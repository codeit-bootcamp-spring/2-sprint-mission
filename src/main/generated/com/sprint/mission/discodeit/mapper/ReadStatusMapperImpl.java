package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-02T10:45:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
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

    @Override
    public ReadStatus toEntity(CreateReadStatusParam createReadStatusParam) {
        if ( createReadStatusParam == null ) {
            return null;
        }

        ReadStatus.ReadStatusBuilder readStatus = ReadStatus.builder();

        readStatus.userId( createReadStatusParam.userId() );
        readStatus.channelId( createReadStatusParam.channelId() );

        return readStatus.build();
    }

    @Override
    public ReadStatusDTO toReadStatusDTO(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        UUID channelId = null;
        Instant updatedAt = null;

        id = readStatus.getId();
        userId = readStatus.getUserId();
        channelId = readStatus.getChannelId();
        updatedAt = readStatus.getUpdatedAt();

        ReadStatusDTO readStatusDTO = new ReadStatusDTO( id, userId, channelId, updatedAt );

        return readStatusDTO;
    }
}
