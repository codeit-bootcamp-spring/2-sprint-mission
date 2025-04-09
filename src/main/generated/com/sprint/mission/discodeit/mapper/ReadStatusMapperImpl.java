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
    date = "2025-04-04T10:27:12+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
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
        Instant lastReadAt = null;

        id = readStatusDTO.id();
        userId = readStatusDTO.userId();
        channelId = readStatusDTO.channelId();
        lastReadAt = readStatusDTO.lastReadAt();

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

        UpdateReadStatusResponseDTO updateReadStatusResponseDTO = new UpdateReadStatusResponseDTO( id, lastReadAt );

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
        Instant lastReadAt = null;

        id = readStatus.getId();
        userId = readStatus.getUserId();
        channelId = readStatus.getChannelId();
        lastReadAt = readStatus.getLastReadAt();

        ReadStatusDTO readStatusDTO = new ReadStatusDTO( id, userId, channelId, lastReadAt );

        return readStatusDTO;
    }
}
