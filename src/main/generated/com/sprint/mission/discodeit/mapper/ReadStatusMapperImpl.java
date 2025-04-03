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
    date = "2025-04-03T20:22:36+0900",
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
        Instant lastReadAt = null;

        userId = createReadStatusRequestDTO.userId();
        channelId = createReadStatusRequestDTO.channelId();
        lastReadAt = createReadStatusRequestDTO.lastReadAt();

        CreateReadStatusParam createReadStatusParam = new CreateReadStatusParam( userId, channelId, lastReadAt );

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

        Instant lastReadAt = null;

        CreateReadStatusResponseDTO createReadStatusResponseDTO = new CreateReadStatusResponseDTO( id, userId, channelId, lastReadAt );

        return createReadStatusResponseDTO;
    }

    @Override
    public UpdateReadStatusResponseDTO toUpdateReadStatusResponseDTO(UpdateReadStatusDTO updateReadStatusDTO) {
        if ( updateReadStatusDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant lastReadAt = null;

        id = updateReadStatusDTO.id();
        lastReadAt = updateReadStatusDTO.lastReadAt();

        UUID userId = null;
        UUID channelId = null;

        UpdateReadStatusResponseDTO updateReadStatusResponseDTO = new UpdateReadStatusResponseDTO( id, userId, channelId, lastReadAt );

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
        readStatus.lastReadAt( createReadStatusParam.lastReadAt() );

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

        id = readStatus.getId();
        userId = readStatus.getUserId();
        channelId = readStatus.getChannelId();

        Instant updatedAt = null;

        ReadStatusDTO readStatusDTO = new ReadStatusDTO( id, userId, channelId, updatedAt );

        return readStatusDTO;
    }
}
