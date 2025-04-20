package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    Channel channel = channelMapper.toEntity(request);
    Channel savedChannel = channelRepository.save(channel);
    return channelMapper.toDto(savedChannel);
  }

  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = channelMapper.toEntity(request);
    Channel savedChannel = channelRepository.save(channel);
    return channelMapper.toDto(savedChannel);
  }

  @Override
  public ChannelDto read(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("[Error] channel is null"));
    return channelMapper.toDto(channel);
  }

  @Override
  public List<ChannelDto> readAllByUserId(UUID userId) {
    List<Channel> channels = readStatusRepository.findAllByUser_Id(userId);
    return channels.stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 존재하지 않는 채널입니다."));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("[Error] PRIVATE 채널은 수정할 수 없습니다.");
    }

    if (request.newName() != null && !request.newName().isEmpty()) {
      channel.updateName(request.newName());
    }

    if (request.newDescription() != null && !request.newDescription().isEmpty()) {
      channel.updateDescription(request.newDescription());
    }

    Channel updatedChannel = channelRepository.save(channel);
    return channelMapper.toDto(updatedChannel);
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 존재하지 않는 채널입니다."));

    messageRepository.deleteAllByChannel_Id(channel.getId());
    readStatusRepository.deleteAllByChannel_Id(channel.getId());

    channelRepository.deleteById(channelId);
  }
  
}
