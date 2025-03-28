package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-28T22:22:38+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public CreateReadStatusParam toReadStatusParam(CreateReadStatusRequestDTO createReadStatusRequestDTO) {
        if ( createReadStatusRequestDTO == null ) {
            return null;
        }

        UUID userId = null;
        UUID channelId = null;

        userId = createReadStatusRequestDTO.userId();
        channelId = createReadStatusRequestDTO.channelId();

        CreateReadStatusParam createReadStatusParam = new CreateReadStatusParam( userId, channelId );

        return createReadStatusParam;
    }

    @Override
    public CreateReadStatusResponseDTO toReadStatusResponseDTO(ReadStatusDTO readStatusDTO) {
        if ( readStatusDTO == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        UUID channelId = null;

        id = readStatusDTO.id();
        userId = readStatusDTO.userId();
        channelId = readStatusDTO.channelId();

        CreateReadStatusResponseDTO createReadStatusResponseDTO = new CreateReadStatusResponseDTO( id, userId, channelId );

        return createReadStatusResponseDTO;
    }

    @Override
    public UpdateReadStatusResponseDTO toUpdateReadStatusResponseDTO(UpdateReadStatusDTO updateReadStatusDTO) {
        if ( updateReadStatusDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;

        id = updateReadStatusDTO.id();
        updatedAt = updateReadStatusDTO.updatedAt();

        UpdateReadStatusResponseDTO updateReadStatusResponseDTO = new UpdateReadStatusResponseDTO( id, updatedAt );

        return updateReadStatusResponseDTO;
    }
}
