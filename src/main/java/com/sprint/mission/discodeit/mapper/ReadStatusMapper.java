package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(source = "channel.id", target = "channelId")
  @Mapping(source = "user.id", target = "userId")
  ReadStatusDto toDto(ReadStatus readStatus);
}
