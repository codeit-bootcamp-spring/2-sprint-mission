package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "participants", ignore = true)
  ChannelDto toDto(Channel channel);

  default ChannelDto toDto(
      Channel channel,
      ReadStatusRepository readStatusRepository,
      UserMapper userMapper,
      MessageRepository messageRepository
  ) {
    ChannelDto result = toDto(channel);

    List<UserDto> participants = readStatusRepository.findAllUsersByChannelId(channel.getId())
        .stream()
        .map(userMapper::toDto)
        .toList();

    Instant lastMessageAt = messageRepository.findFirstByChannelIdOrderByCreatedAtDesc(
            channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);

    return new ChannelDto(
        result.id(),
        result.type(),
        result.name(),
        result.description(),
        participants,
        lastMessageAt
    );
  }
}
