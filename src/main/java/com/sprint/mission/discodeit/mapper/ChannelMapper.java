package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
        messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .map(message -> {
              return userMapper.toDto(message.getAuthor());
            })
            .toList(),
        messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .map(message -> {
              return message.getUpdatedAt();
            })
            .findAny().orElse(null)
    );
  }
}

