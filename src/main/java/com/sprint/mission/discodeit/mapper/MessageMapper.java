package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BinaryContentMapper.class})
public interface MessageMapper {

  @Mapping(target = "channelId", expression = "java(message.getChannel().getId())")
  @Mapping(source = "author", target = "author")
  @Mapping(source = "attachments", target = "attachments")
  MessageDto toDto(Message message);
}
