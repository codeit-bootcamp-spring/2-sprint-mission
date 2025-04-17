package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, ChannelMapper.class})
public interface MessageMapper {

  @Mapping(source = "channel", target = "channelId")
  MessageDto toDto(Message message);
}
