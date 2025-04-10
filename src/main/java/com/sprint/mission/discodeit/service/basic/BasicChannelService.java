package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

  @Override
  public Channel create(PublicChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    return channelRepository.save(channel);
  }

  @Override
  public Channel create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    request.participantIds().stream()
        .filter(userRepository::existsByKey)
        .map(userKey -> new ReadStatus(userKey, createdChannel.getId(), Instant.EPOCH))
        .forEach(readStatusRepository::save);

    return createdChannel;
  }


  @Override
  public ChannelDto read(UUID channelKey) {
    Channel channel = channelRepository.findByKey(channelKey);
    if (channel == null) {
      throw new IllegalArgumentException("[Error] channel is null");
    }
    return createReadChannelResponse(channel);
  }

  @Override
  public List<ChannelDto> readAllByUserKey(UUID userKey) {
    List<UUID> privateChannelKeys = readStatusRepository.findAllByUserKey(userKey).stream()
        .map(ReadStatus::getChannelId)
        .toList();
    return channelRepository.findAll().stream()
        .filter(
            channel -> channel.getType().equals(ChannelType.PUBLIC) || privateChannelKeys.contains(
                channel.getId()))
        .map(this::createReadChannelResponse)
        .toList();
  }

  @Override
  public Channel update(UUID channelKey, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findByKey(channelKey);
    if (channel == null) {
      throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
    }
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("[Error] PRIVATE 채널은 수정할 수 없습니다.");
    }
    if (!request.newName().isEmpty()) {
      channel.updateName(request.newName());
    }
    if (!request.newDescription().isEmpty()) {
      channel.updateIntroduction(request.newDescription());
    }
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public void delete(UUID channelKey) {
    Channel channel = channelRepository.findByKey(channelKey);
    if (channel == null) {
      throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
    }

    List<Message> messages = messageRepository.findAllByChannelKey(channelKey);
    for (Message message : messages) {
      messageRepository.delete(message.getId());
    }

    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelKey(channelKey);
    for (ReadStatus readStatus : readStatuses) {
      readStatusRepository.delete(readStatus.getId());
    }

    channelRepository.delete(channel.getId());
  }

  private ChannelDto createReadChannelResponse(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelKey(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);
    List<UUID> memberKeys = null;

    if (channel.getType() == ChannelType.PRIVATE) {
      memberKeys = readStatusRepository.findAllByChannelKey(channel.getId()).stream()
          .map(ReadStatus::getUserId)
          .toList();
    }

    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), memberKeys, lastMessageAt);
  }

}
