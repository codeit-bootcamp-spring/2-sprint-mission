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
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T17:24:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public CreateReadStatusCommand toCreateReadStatusCommand(CreateReadStatusRequestDTO createReadStatusRequestDTO) {
        if ( createReadStatusRequestDTO == null ) {
            return null;
        }

        UUID userId = null;
        UUID channelId = null;
        Instant lastReadAt = null;

        userId = createReadStatusRequestDTO.userId();
        channelId = createReadStatusRequestDTO.channelId();
        lastReadAt = createReadStatusRequestDTO.lastReadAt();

        CreateReadStatusCommand createReadStatusCommand = new CreateReadStatusCommand( userId, channelId, lastReadAt );

        return createReadStatusCommand;
    }

    @Override
    public CreateReadStatusResponseDTO toReadStatusResponseDTO(CreateReadStatusResult createReadStatusResult) {
        if ( createReadStatusResult == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        UUID channelId = null;
        Instant lastReadAt = null;

        id = createReadStatusResult.id();
        userId = createReadStatusResult.userId();
        channelId = createReadStatusResult.channelId();
        lastReadAt = createReadStatusResult.lastReadAt();

        CreateReadStatusResponseDTO createReadStatusResponseDTO = new CreateReadStatusResponseDTO( id, userId, channelId, lastReadAt );

        return createReadStatusResponseDTO;
    }

    @Override
    public UpdateReadStatusResponseDTO toUpdateReadStatusResponseDTO(UpdateReadStatusResult updateReadStatusResult) {
        if ( updateReadStatusResult == null ) {
            return null;
        }

        UUID id = null;
        Instant lastReadAt = null;

        id = updateReadStatusResult.id();
        lastReadAt = updateReadStatusResult.lastReadAt();

        UpdateReadStatusResponseDTO updateReadStatusResponseDTO = new UpdateReadStatusResponseDTO( id, lastReadAt );

        return updateReadStatusResponseDTO;
    }

    @Override
    public FindReadStatusResult toFindReadStatusResult(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID channelId = null;
        UUID id = null;
        Instant lastReadAt = null;

        userId = readStatusUserId( readStatus );
        channelId = readStatusChannelId( readStatus );
        id = readStatus.getId();
        lastReadAt = readStatus.getLastReadAt();

        FindReadStatusResult findReadStatusResult = new FindReadStatusResult( id, userId, channelId, lastReadAt );

        return findReadStatusResult;
    }

    @Override
    public FindReadStatusResponseDTO toFindReadStatusResponseDTO(FindReadStatusResult findReadStatusResult) {
        if ( findReadStatusResult == null ) {
            return null;
        }

        UUID id = null;
        UUID userId = null;
        UUID channelId = null;
        Instant lastReadAt = null;

        id = findReadStatusResult.id();
        userId = findReadStatusResult.userId();
        channelId = findReadStatusResult.channelId();
        lastReadAt = findReadStatusResult.lastReadAt();

        FindReadStatusResponseDTO findReadStatusResponseDTO = new FindReadStatusResponseDTO( id, userId, channelId, lastReadAt );

        return findReadStatusResponseDTO;
    }

    @Override
    public CreateReadStatusResult toCreateReadStatusResult(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID channelId = null;
        UUID id = null;
        Instant lastReadAt = null;

        userId = readStatusUserId( readStatus );
        channelId = readStatusChannelId( readStatus );
        id = readStatus.getId();
        lastReadAt = readStatus.getLastReadAt();

        CreateReadStatusResult createReadStatusResult = new CreateReadStatusResult( id, userId, channelId, lastReadAt );

        return createReadStatusResult;
    }

    @Override
    public UpdateReadStatusResult toUpdateReadStatusResult(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID channelId = null;
        UUID id = null;
        Instant lastReadAt = null;

        userId = readStatusUserId( readStatus );
        channelId = readStatusChannelId( readStatus );
        id = readStatus.getId();
        lastReadAt = readStatus.getLastReadAt();

        UpdateReadStatusResult updateReadStatusResult = new UpdateReadStatusResult( id, userId, channelId, lastReadAt );

        return updateReadStatusResult;
    }

    @Override
    public UpdateReadStatusCommand toUpdateReadStatusCommand(UpdateReadStatusRequestDTO updateReadStatusRequestDTO) {
        if ( updateReadStatusRequestDTO == null ) {
            return null;
        }

        Instant newLastReadAt = null;

        newLastReadAt = updateReadStatusRequestDTO.newLastReadAt();

        UpdateReadStatusCommand updateReadStatusCommand = new UpdateReadStatusCommand( newLastReadAt );

        return updateReadStatusCommand;
    }

    private UUID readStatusUserId(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }
        User user = readStatus.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID readStatusChannelId(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }
        Channel channel = readStatus.getChannel();
        if ( channel == null ) {
            return null;
        }
        UUID id = channel.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
