package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreateChannelParam;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelParam;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelParam;
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
    date = "2025-04-04T10:27:12+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public CreateChannelParam toChannelParam(CreatePublicChannelRequestDTO createChannelRequestDTO) {
        if ( createChannelRequestDTO == null ) {
            return null;
        }

        String name = null;
        String description = null;

        name = createChannelRequestDTO.name();
        description = createChannelRequestDTO.description();

        CreateChannelParam createChannelParam = new CreateChannelParam( name, description );

        return createChannelParam;
    }

    @Override
    public CreatePublicChannelResponseDTO toChannelResponseDTO(ChannelDTO channelDTO) {
        if ( channelDTO == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<UUID> participantIds = null;
        Instant lastMessageAt = null;

        id = channelDTO.id();
        type = channelDTO.type();
        name = channelDTO.name();
        description = channelDTO.description();
        List<UUID> list = channelDTO.participantIds();
        if ( list != null ) {
            participantIds = new ArrayList<UUID>( list );
        }
        lastMessageAt = channelDTO.lastMessageAt();

        CreatePublicChannelResponseDTO createPublicChannelResponseDTO = new CreatePublicChannelResponseDTO( id, type, name, description, participantIds, lastMessageAt );

        return createPublicChannelResponseDTO;
    }

    @Override
    public CreatePrivateChannelParam toPrivateChannelParam(CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
        if ( createPrivateChannelRequestDTO == null ) {
            return null;
        }

        List<UUID> participantIds = null;

        List<UUID> list = createPrivateChannelRequestDTO.participantIds();
        if ( list != null ) {
            participantIds = new ArrayList<UUID>( list );
        }

        CreatePrivateChannelParam createPrivateChannelParam = new CreatePrivateChannelParam( participantIds );

        return createPrivateChannelParam;
    }

    @Override
    public CreatePrivateChannelResponseDTO toPrivateChannelResponseDTO(PrivateChannelDTO privateChannelDTO) {
        if ( privateChannelDTO == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<UUID> participantIds = null;
        Instant lastMessageAt = null;

        id = privateChannelDTO.id();
        type = privateChannelDTO.type();
        name = privateChannelDTO.name();
        description = privateChannelDTO.description();
        List<UUID> list = privateChannelDTO.participantIds();
        if ( list != null ) {
            participantIds = new ArrayList<UUID>( list );
        }
        lastMessageAt = privateChannelDTO.lastMessageAt();

        CreatePrivateChannelResponseDTO createPrivateChannelResponseDTO = new CreatePrivateChannelResponseDTO( id, type, name, description, participantIds, lastMessageAt );

        return createPrivateChannelResponseDTO;
    }

    @Override
    public UpdateChannelParam toUpdateChannelParam(UpdateChannelRequestDTO updateChannelRequestDTO) {
        if ( updateChannelRequestDTO == null ) {
            return null;
        }

        String newName = null;
        String newDescription = null;

        newName = updateChannelRequestDTO.newName();
        newDescription = updateChannelRequestDTO.newDescription();

        UpdateChannelParam updateChannelParam = new UpdateChannelParam( newName, newDescription );

        return updateChannelParam;
    }

    @Override
    public UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelDTO updateChannelDTO) {
        if ( updateChannelDTO == null ) {
            return null;
        }

        UUID id = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<UUID> participantIds = null;
        Instant lastMessageAt = null;

        id = updateChannelDTO.id();
        type = updateChannelDTO.type();
        name = updateChannelDTO.name();
        description = updateChannelDTO.description();
        List<UUID> list = updateChannelDTO.participantIds();
        if ( list != null ) {
            participantIds = new ArrayList<UUID>( list );
        }
        lastMessageAt = updateChannelDTO.lastMessageAt();

        UpdateChannelResponseDTO updateChannelResponseDTO = new UpdateChannelResponseDTO( id, type, name, description, participantIds, lastMessageAt );

        return updateChannelResponseDTO;
    }

    @Override
    public ChannelDTO toChannelDTO(Channel channel) {
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

        List<UUID> participantIds = null;
        Instant lastMessageAt = null;

        ChannelDTO channelDTO = new ChannelDTO( id, type, name, description, participantIds, lastMessageAt );

        return channelDTO;
    }

    @Override
    public UpdateChannelDTO toUpdateChannelDTO(Channel channel, Instant lastMessageAt) {
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

        List<UUID> participantIds = null;

        UpdateChannelDTO updateChannelDTO = new UpdateChannelDTO( id, type, name, description, participantIds, lastMessageAt1 );

        return updateChannelDTO;
    }
}
