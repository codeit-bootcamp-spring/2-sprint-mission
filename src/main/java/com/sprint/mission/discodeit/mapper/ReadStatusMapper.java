package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ChannelMapper.class})
public interface ReadStatusMapper {

  @Mapping(source = "user", target = "userId")
  @Mapping(source = "channel", target = "channelId")
  ReadStatusDto toDto(ReadStatus readStatus);
}
