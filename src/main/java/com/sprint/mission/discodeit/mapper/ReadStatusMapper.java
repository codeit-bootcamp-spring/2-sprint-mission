package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.FindReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusResult;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);

  CreateReadStatusCommand toCreateReadStatusCommand(
      CreateReadStatusRequestDTO createReadStatusRequestDTO);

  CreateReadStatusResponseDTO toReadStatusResponseDTO(
      CreateReadStatusResult createReadStatusResult);

  UpdateReadStatusResponseDTO toUpdateReadStatusResponseDTO(
      UpdateReadStatusResult updateReadStatusResult);

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "channel.id", target = "channelId")
  FindReadStatusResult toFindReadStatusResult(ReadStatus readStatus);

  FindReadStatusResponseDTO toFindReadStatusResponseDTO(FindReadStatusResult findReadStatusResult);

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "channel.id", target = "channelId")
  CreateReadStatusResult toCreateReadStatusResult(ReadStatus readStatus);

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "channel.id", target = "channelId")
  UpdateReadStatusResult toUpdateReadStatusResult(ReadStatus readStatus);

  UpdateReadStatusCommand toUpdateReadStatusCommand(
      UpdateReadStatusRequestDTO updateReadStatusRequestDTO);
}
