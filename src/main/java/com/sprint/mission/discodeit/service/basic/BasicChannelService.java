package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByUserIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusService readStatusService;
  private final BasicBinaryContentService basicBinaryContentService;

  @Override
  public ChannelResponse create(PrivateChannelRequest privateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel channelSave = channelRepository.save(channel);
    List<UUID> participantIds = privateRequest.participantIds();
    participantIds.forEach(userId ->
        readStatusService.create(
            new ReadStatusCreateRequest(userId, channelSave.getId(), null)));
    return ChannelResponse.of(channel, participantIds, Instant.now());
  }

  @Override
  public ChannelResponse create(PublicChannelRequest publicRequest) {
    Channel channel = new Channel(ChannelType.PUBLIC, publicRequest.name(),
        publicRequest.description());
    channelRepository.save(channel);
    return ChannelResponse.of(channel, null, Instant.now());
  }

  @Override
  public ChannelByUserIdResponse find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));
    Instant lastMessageAt = findLastMessageAt(channel.getId());

    String name;
    String description;
    List<UUID> participantIds;

    if (channel.getType() == ChannelType.PRIVATE) {
      name = null;
      description = null;
      participantIds = readStatusService.findAllUserByChannelId(channelId);
    } else {
      name = channel.getName();
      description = channel.getDescription();
      participantIds = null;
    }

    return ChannelByUserIdResponse.of(channel, name, description, participantIds, lastMessageAt);
  }

  @Override
  public List<ChannelByUserIdResponse> findAllByUserId(UUID userId) {
    List<UUID> joinedChannelIds = readStatusService.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId).toList();
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC) || joinedChannelIds.contains(
                channel.getId()))
        .map(channel -> {
          Instant lastMessageAt = findLastMessageAt(channel.getId());

          String name;
          String description;
          List<UUID> participantIds;

          if (channel.getType() == ChannelType.PRIVATE) {
            name = null;
            description = null;
            participantIds = readStatusService.findAllUserByChannelId(channel.getId());
          } else {
            name = channel.getName();
            description = channel.getDescription();
            participantIds = null;
          }

          return ChannelByUserIdResponse.of(channel, name, description, participantIds,
              lastMessageAt);
        }).toList();
  }

  @Override
  public ChannelResponse update(UUID id, ChannelUpdateRequest updateRequest) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Channel을 찾을 수 없음"));
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("비공개 채널은 수정 불가능");
    }
    String newName = updateRequest.newName();
    String newDescription = updateRequest.newDescription();

    channel.update(newName, newDescription);
    channelRepository.save(channel);

    Instant lastMessageAt = findLastMessageAt(channel.getId());
    return ChannelResponse.of(channel, null, lastMessageAt);
  }

  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + "에 해당하는 Channel을 찾을 수 없음");
    }
    messageRepository.findAllByChannelId(channelId)
        .forEach(message -> {
          message.getAttachmentIds().forEach(basicBinaryContentService::delete);
          messageRepository.deleteById(message.getId());
        });

    readStatusService.findAllByChannelId(channelId).forEach(readStatusService::delete);
    channelRepository.deleteById(channelId);
  }

  private Instant findLastMessageAt(UUID channelId) {
    List<Message> messageList = messageRepository.findAll().stream()
        .filter(message -> message.getChannelId().equals(channelId)).toList();
    if (messageList.isEmpty()) {
      return null;
    }
    return messageList.stream().map(Message::getCreatedAt).max(Instant::compareTo).orElse(null);
  }
}
