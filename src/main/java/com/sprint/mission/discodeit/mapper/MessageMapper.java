package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.message.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  @Mapping(target = "channelId", source = "channel.id")
  MessageDto toResponse(Message message);
}
