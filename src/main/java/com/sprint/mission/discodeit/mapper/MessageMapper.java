package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  @Mapping(source = "channel.id", target = "channelId")
  MessageDto toDto(Message message);

  default MessageDto toDto(Message message, UserMapper userMapper) {
    boolean online = Optional.ofNullable(message.getAuthor())
        .map(User::getStatus)
        .map(UserStatus::isOnline)
        .orElse(false);

    UserDto authorDto = message.getAuthor() != null ?
        userMapper.toDto(message.getAuthor(), online) : null;

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        authorDto,
        message.getAttachments().stream()
            .map(attachment -> new BinaryContentDto(attachment.getId(), attachment.getFileName(),
                attachment.getSize(), attachment.getContentType()))
            .collect(Collectors.toList())
    );
  }
}