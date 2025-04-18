package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(CreatePrivateChannelRequest request) {
    Channel channel = channelRepository.save(
        Channel.builder()
            .type(ChannelType.PRIVATE)
            .name("")
            .description("")
            .build());

    request.participantIds().forEach(userId -> {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException("UserId :" + userId + "not found"));
      ReadStatus readStatus = ReadStatus.builder()
          .user(user)
          .channel(channel)
          .lastReadAt(Instant.now())
          .build();
      readStatusRepository.save(readStatus);
    });
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto createPublicChannel(CreatePublicChannelRequest request) {
    Channel channel = channelRepository.save(
        Channel.builder()
            .type(ChannelType.PUBLIC)
            .name(request.name())
            .description(request.description())
            .build());

    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto findChannelById(UUID channelId) {
    return channelMapper.toDto(findChannelOrThrow(channelId));
  }

  @Override
  public List<ChannelDto> getAllChannels() {
    List<Channel> channels = channelRepository.findAll();

    return channels.stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("UserId: " + userId + " not found");
    }

    List<ChannelDto> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC).stream()
        .map(channelMapper::toDto)
        .toList();

    List<ChannelDto> privateChannels =
        readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .filter(channel -> channel.getType() == ChannelType.PRIVATE)
            .map(channelMapper::toDto)
            .toList();

    return Stream.concat(publicChannels.stream(), privateChannels.stream())
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId, UpdateChannelRequest request) {
    Channel channel = findChannelOrThrow(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    if (request.newName() != null) {
      channel.updateChannelName(request.newName());
    }
    if (request.newDescription() != null) {
      channel.updateDescription(request.newDescription());
    }

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    validateChannelExists(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    messageRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
  }

  @Override
  public void validateChannelExists(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("ChannelId: " + channelId + " not found");
    }
  }

  private Channel findChannelOrThrow(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("ChannelId: " + channelId + "not found"));
  }
}
