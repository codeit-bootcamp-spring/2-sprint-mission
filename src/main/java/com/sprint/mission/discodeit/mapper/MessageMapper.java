package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  @Mapping(source = "channel.id", target = "channelId")
  MessageDto toDto(Message message);
  
}