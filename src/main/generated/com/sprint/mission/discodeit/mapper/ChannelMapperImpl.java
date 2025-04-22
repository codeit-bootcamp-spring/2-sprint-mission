package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.FindChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-17T11:36:05+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public CreatePublicChannelCommand toCreatePublicChannelCommand(CreatePublicChannelRequestDTO createPublicChannelRequestDTO) {
        if ( createPublicChannelRequestDTO == null ) {
            return null;
        }

        String name = null;
        String description = null;

        name = createPublicChannelRequestDTO.name();
        description = createPublicChannelRequestDTO.description();

        CreatePublicChannelCommand createPublicChannelCommand = new CreatePublicChannelCommand( name, description );

        return createPublicChannelCommand;
    }

    @Override
    public CreatePublicChannelResponseDTO toCreatePublicChannelResponseDTO(CreatePublicChannelResult createPublicChannelResult) {
        if ( createPublicChannelResult == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<FindUserResult> participants = null;
        Instant lastMessageAt = null;

        id = createPublicChannelResult.id();
        type = createPublicChannelResult.type();
        name = createPublicChannelResult.name();
        description = createPublicChannelResult.description();
        List<FindUserResult> list = createPublicChannelResult.participants();
        if ( list != null ) {
            participants = new ArrayList<FindUserResult>( list );
        }
        lastMessageAt = createPublicChannelResult.lastMessageAt();

        CreatePublicChannelResponseDTO createPublicChannelResponseDTO = new CreatePublicChannelResponseDTO( id, type, name, description, participants, lastMessageAt );

        return createPublicChannelResponseDTO;
    }

    @Override
    public CreatePrivateChannelCommand toCreatePrivateChannelCommand(CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
        if ( createPrivateChannelRequestDTO == null ) {
            return null;
        }

        List<UUID> participantIds = null;

        List<UUID> list = createPrivateChannelRequestDTO.participantIds();
        if ( list != null ) {
            participantIds = new ArrayList<UUID>( list );
        }

        CreatePrivateChannelCommand createPrivateChannelCommand = new CreatePrivateChannelCommand( participantIds );

        return createPrivateChannelCommand;
    }

    @Override
    public CreatePrivateChannelResponseDTO toCreatePrivateChannelResponseDTO(CreatePrivateChannelResult createPrivateChannelResult) {
        if ( createPrivateChannelResult == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<FindUserResult> participants = null;
        Instant lastMessageAt = null;

        id = createPrivateChannelResult.id();
        type = createPrivateChannelResult.type();
        name = createPrivateChannelResult.name();
        description = createPrivateChannelResult.description();
        List<FindUserResult> list = createPrivateChannelResult.participants();
        if ( list != null ) {
            participants = new ArrayList<FindUserResult>( list );
        }
        lastMessageAt = createPrivateChannelResult.lastMessageAt();

        CreatePrivateChannelResponseDTO createPrivateChannelResponseDTO = new CreatePrivateChannelResponseDTO( id, type, name, description, participants, lastMessageAt );

        return createPrivateChannelResponseDTO;
    }

    @Override
    public UpdateChannelCommand toUpdateChannelCommand(UpdateChannelRequestDTO updateChannelRequestDTO) {
        if ( updateChannelRequestDTO == null ) {
            return null;
        }

        String newName = null;
        String newDescription = null;

        newName = updateChannelRequestDTO.newName();
        newDescription = updateChannelRequestDTO.newDescription();

        UpdateChannelCommand updateChannelCommand = new UpdateChannelCommand( newName, newDescription );

        return updateChannelCommand;
    }

    @Override
    public UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelResult updateChannelResult) {
        if ( updateChannelResult == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<FindUserResult> participants = null;
        Instant lastMessageAt = null;

        id = updateChannelResult.id();
        type = updateChannelResult.type();
        name = updateChannelResult.name();
        description = updateChannelResult.description();
        List<FindUserResult> list = updateChannelResult.participants();
        if ( list != null ) {
            participants = new ArrayList<FindUserResult>( list );
        }
        lastMessageAt = updateChannelResult.lastMessageAt();

        UpdateChannelResponseDTO updateChannelResponseDTO = new UpdateChannelResponseDTO( id, type, name, description, participants, lastMessageAt );

        return updateChannelResponseDTO;
    }

    @Override
    public UpdateChannelResult toUpdateChannelResult(Channel channel, Instant lastMessageAt) {
        if ( channel == null && lastMessageAt == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        if ( channel != null ) {
            id = channel.getId();
            type = channel.getType();
            name = channel.getName();
            description = channel.getDescription();
        }
        Instant lastMessageAt1 = null;
        lastMessageAt1 = lastMessageAt;

        List<FindUserResult> participants = null;

        UpdateChannelResult updateChannelResult = new UpdateChannelResult( id, type, name, description, participants, lastMessageAt1 );

        return updateChannelResult;
    }

    @Override
    public FindChannelResponseDTO toFindChannelResponseDTO(FindChannelResult findChannelResult) {
        if ( findChannelResult == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<FindUserResult> participants = null;
        Instant lastMessageAt = null;

        id = findChannelResult.id();
        type = findChannelResult.type();
        name = findChannelResult.name();
        description = findChannelResult.description();
        List<FindUserResult> list = findChannelResult.participants();
        if ( list != null ) {
            participants = new ArrayList<FindUserResult>( list );
        }
        lastMessageAt = findChannelResult.lastMessageAt();

        FindChannelResponseDTO findChannelResponseDTO = new FindChannelResponseDTO( id, type, name, description, participants, lastMessageAt );

        return findChannelResponseDTO;
    }

    @Override
    public FindChannelResult toFindChannelResult(Channel channel, Instant lastMessageAt, List<FindUserResult> participants) {
        if ( channel == null && lastMessageAt == null && participants == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        if ( channel != null ) {
            id = channel.getId();
            type = channel.getType();
            name = channel.getName();
            description = channel.getDescription();
        }
        Instant lastMessageAt1 = null;
        lastMessageAt1 = lastMessageAt;
        List<FindUserResult> participants1 = null;
        List<FindUserResult> list = participants;
        if ( list != null ) {
            participants1 = new ArrayList<FindUserResult>( list );
        }

        FindChannelResult findChannelResult = new FindChannelResult( id, type, name, description, participants1, lastMessageAt1 );

        return findChannelResult;
    }

    @Override
    public CreatePrivateChannelResult toCreatePrivateChannelResult(Channel channel, Instant lastMessageAt, List<FindUserResult> participants) {
        if ( channel == null && lastMessageAt == null && participants == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        if ( channel != null ) {
            id = channel.getId();
            type = channel.getType();
            name = channel.getName();
            description = channel.getDescription();
        }
        Instant lastMessageAt1 = null;
        lastMessageAt1 = lastMessageAt;
        List<FindUserResult> participants1 = null;
        List<FindUserResult> list = participants;
        if ( list != null ) {
            participants1 = new ArrayList<FindUserResult>( list );
        }

        CreatePrivateChannelResult createPrivateChannelResult = new CreatePrivateChannelResult( id, type, name, description, participants1, lastMessageAt1 );

        return createPrivateChannelResult;
    }

    @Override
    public CreatePublicChannelResult toCreatePublicChannelResult(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = channel.getId();
        type = channel.getType();
        name = channel.getName();
        description = channel.getDescription();

        List<FindUserResult> participants = null;
        Instant lastMessageAt = null;

        CreatePublicChannelResult createPublicChannelResult = new CreatePublicChannelResult( id, type, name, description, participants, lastMessageAt );

        return createPublicChannelResult;
    }
}
