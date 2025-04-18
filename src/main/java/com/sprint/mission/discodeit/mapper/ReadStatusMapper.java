package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mappings({
      @Mapping(source = "user.id", target = "userId"),
      @Mapping(source = "channel.id", target = "channelId")
  })
  ReadStatusDto toDto(ReadStatus readStatus);

  @Mappings({
      @Mapping(source = "user.id", target = "userId"),
      @Mapping(source = "channel.id", target = "channelId")
  })
  List<ReadStatusDto> toDtoList(List<ReadStatus> readStatus);
}
