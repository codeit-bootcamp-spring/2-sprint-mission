package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.controller.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.controller.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity._Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity._Message;
import com.sprint.mission.discodeit.entity._ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public _Channel create(PublicChannelCreateRequest request) {
    String name = request.getName();
    String description = request.getDescription();
    _Channel channel = new _Channel(ChannelType.PUBLIC, name, description);

    return channelRepository.save(channel);
  }

  @Override
  public _Channel create(PrivateChannelCreateRequest request) {
    _Channel channel = new _Channel(ChannelType.PRIVATE, null, null);
    _Channel createdChannel = channelRepository.save(channel);

    request.getParticipantIds().stream()
        .map(userId -> new _ReadStatus(userId, createdChannel.getId(), OffsetDateTime.MIN))
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  @Override
  public ChannelDto find(UUID getChannelId) {
    return channelRepository.findById(getChannelId)
        .map(this::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + getChannelId + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedgetChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(_ReadStatus::getGetChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedgetChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public _Channel update(UUID getChannelId, PublicChannelUpdateRequest request) {
    String newName = request.getNewName();
    String newDescription = request.getNewDescription();
    _Channel channel = channelRepository.findById(getChannelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + getChannelId + " not found"));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(newName, newDescription);
    return channelRepository.save(channel);
  }

  @Override
  public void delete(UUID getChannelId) {
    _Channel channel = channelRepository.findById(getChannelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + getChannelId + " not found"));

    messageRepository.deleteAllBygetChannelId(channel.getId());
    readStatusRepository.deleteAllBygetChannelId(channel.getId());

    channelRepository.deleteById(getChannelId);
  }

  private ChannelDto toDto(_Channel channel) {
    OffsetDateTime lastMessageAt = messageRepository.findAllBygetChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(_Message::getCreatedAt).reversed())
        .map(_Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(OffsetDateTime.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllBygetChannelId(channel.getId())
          .stream()
          .map(_ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
