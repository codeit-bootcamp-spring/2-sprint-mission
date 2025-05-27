package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.InvalidChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = Channel.create(ChannelType.PUBLIC, request.name(), request.description());
    Channel createdChannel = channelRepository.save(channel);
    return channelMapper.toDto(createdChannel);
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = Channel.create(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> ReadStatus.create(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    return channelMapper.toDto(createdChannel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw InvalidChannelUpdateException.byId(channelId);
    }

    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

    if (messageRepository.existsByChannelId(channel.getId())) {
      messageRepository.deleteAllByChannelId(channelId);
    }
    if (channel.getType() == ChannelType.PRIVATE) {
      readStatusRepository.deleteAllByChannelId(channelId);
    }

    channelRepository.deleteById(channelId);
  }


}
