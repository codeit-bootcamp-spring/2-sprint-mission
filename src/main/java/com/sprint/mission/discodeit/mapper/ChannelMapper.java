package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  public ChannelDto toDto(Channel channel) {
    // 1. participantIds
    List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
        ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(user -> user.getId())
        .toList()
        : List.of();

    // 2. lastMessageAt
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(null); // or Instant.MIN;

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