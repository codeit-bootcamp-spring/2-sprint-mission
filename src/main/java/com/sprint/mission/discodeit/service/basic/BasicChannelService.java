package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.MessageNotExistsInChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  public Channel createPublic(PublicChannelCreateRequest request) {
    if (channelRepository.findAll().stream()
        .anyMatch(channel -> channel.getName().equals(request.name()))) {
      throw new ChannelNameAlreadyExistsException("같은 이름을 가진 채널이 있습니다.");
    }
    Channel channel = new Channel(PUBLIC, request.name(), request.description());

    return channelRepository.save(channel);
  }

  public Channel createPrivate(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(PRIVATE, null, null);
    channelRepository.save(channel);

    request.participantIds().forEach(userId -> {
      readStatusRepository.save(
          new ReadStatusCreateRequest(userId, channel.getId(), null));
    });

    return channel;
  }

  public ChannelResponse find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId);

    Optional<Message> latestMessage = messageRepository.findAll().stream()
        .filter(message -> channelId.equals(message.getChannelId()))
        .max(Comparator.comparing(Message::getCreatedAt));

    if (latestMessage.isEmpty()) {
      throw new MessageNotExistsInChannelException("채널에 메세지가 없습니다");
    }

    return toDto(channel);
  }

  public List<ChannelResponse> findAllByUserID(UUID userId) {
    List<Channel> channels = channelRepository.findAll();

    Set<UUID> userPrivateChannelIds = readStatusRepository.findAll().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .map(ReadStatus::getChannelId)
        .collect(Collectors.toSet());

    return channels.stream()
        .filter(channel ->
            channel.getType() == PUBLIC || userPrivateChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = Optional.ofNullable(channelRepository.findById(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(
            "Channel with id " + channelId + " not found"));

    if (channel.getType() == PRIVATE) {
      throw new PrivateChannelUpdateException("PRIVATE 채널은 수정할 수 없습니다");
    }

    return channelRepository.update(channel, request.newName(),
        request.newDescription());
  }

  public void delete(UUID channelId) {
    channelRepository.findById(channelId);

    channelRepository.delete(channelId);
    messageRepository.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .forEach(message -> messageRepository.delete(message.getId()));
    readStatusRepository.findAll().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));
  }

  private ChannelResponse toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new ChannelResponse(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
