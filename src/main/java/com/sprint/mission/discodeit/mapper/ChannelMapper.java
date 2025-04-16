package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants(channel),
        lastMessageAt(channel)
    );
  }

  private List<UserDto> participants(Channel channel) {
    return readStatusRepository.findByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();
  }

  private Instant lastMessageAt(Channel channel) {
    return messageRepository.findByChannelId(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(null);
  }
}
