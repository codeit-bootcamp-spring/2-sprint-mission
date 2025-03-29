package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {
    ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);

    CreateReadStatusParam toReadStatusParam(CreateReadStatusRequestDTO createReadStatusRequestDTO);
    CreateReadStatusResponseDTO toReadStatusResponseDTO(ReadStatusDTO readStatusDTO);

    UpdateReadStatusResponseDTO toUpdateReadStatusResponseDTO(UpdateReadStatusDTO updateReadStatusDTO);

    ReadStatus toEntity(CreateReadStatusParam createReadStatusParam);
    ReadStatusDTO toReadStatusDTO(ReadStatus readStatus);
}
