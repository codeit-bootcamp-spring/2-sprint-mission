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
    date = "2025-04-02T10:45:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public CreateChannelParam toChannelParam(CreatePublicChannelRequestDTO createChannelRequestDTO) {
        if ( createChannelRequestDTO == null ) {
            return null;
        }

        ChannelType type = null;
        String name = null;
        String description = null;

        type = createChannelRequestDTO.type();
        name = createChannelRequestDTO.name();
        description = createChannelRequestDTO.description();

        CreateChannelParam createChannelParam = new CreateChannelParam( type, name, description );

        return createChannelParam;
    }

    @Override
    public CreatePublicChannelResponseDTO toChannelResponseDTO(ChannelDTO channelDTO) {
        if ( channelDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = channelDTO.id();
        createdAt = channelDTO.createdAt();
        type = channelDTO.type();
        name = channelDTO.name();
        description = channelDTO.description();

        CreatePublicChannelResponseDTO createPublicChannelResponseDTO = new CreatePublicChannelResponseDTO( id, createdAt, type, name, description );

        return createPublicChannelResponseDTO;
    }

    @Override
    public CreatePrivateChannelParam toPrivateChannelParam(CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
        if ( createPrivateChannelRequestDTO == null ) {
            return null;
        }

        ChannelType type = null;
        String name = null;
        String description = null;
        List<UUID> userIds = null;

        type = createPrivateChannelRequestDTO.type();
        name = createPrivateChannelRequestDTO.name();
        description = createPrivateChannelRequestDTO.description();
        List<UUID> list = createPrivateChannelRequestDTO.userIds();
        if ( list != null ) {
            userIds = new ArrayList<UUID>( list );
        }

        CreatePrivateChannelParam createPrivateChannelParam = new CreatePrivateChannelParam( type, name, description, userIds );

        return createPrivateChannelParam;
    }

    @Override
    public CreatePrivateChannelResponseDTO toPrivateChannelResponseDTO(PrivateChannelDTO privateChannelDTO) {
        if ( privateChannelDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        ChannelType type = null;
        String name = null;
        String description = null;
        List<UUID> userIds = null;

        id = privateChannelDTO.id();
        createdAt = privateChannelDTO.createdAt();
        type = privateChannelDTO.type();
        name = privateChannelDTO.name();
        description = privateChannelDTO.description();
        List<UUID> list = privateChannelDTO.userIds();
        if ( list != null ) {
            userIds = new ArrayList<UUID>( list );
        }

        CreatePrivateChannelResponseDTO createPrivateChannelResponseDTO = new CreatePrivateChannelResponseDTO( id, createdAt, type, name, description, userIds );

        return createPrivateChannelResponseDTO;
    }

    @Override
    public UpdateChannelParam toUpdateChannelParam(UpdateChannelRequestDTO updateChannelRequestDTO) {
        if ( updateChannelRequestDTO == null ) {
            return null;
        }

        String name = null;
        String description = null;

        name = updateChannelRequestDTO.name();
        description = updateChannelRequestDTO.description();

        UpdateChannelParam updateChannelParam = new UpdateChannelParam( name, description );

        return updateChannelParam;
    }

    @Override
    public UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelDTO updateChannelDTO) {
        if ( updateChannelDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = updateChannelDTO.id();
        updatedAt = updateChannelDTO.updatedAt();
        type = updateChannelDTO.type();
        name = updateChannelDTO.name();
        description = updateChannelDTO.description();

        UpdateChannelResponseDTO updateChannelResponseDTO = new UpdateChannelResponseDTO( id, updatedAt, type, name, description );

        return updateChannelResponseDTO;
    }

    @Override
    public ChannelDTO toChannelDTO(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = channel.getId();
        createdAt = channel.getCreatedAt();
        updatedAt = channel.getUpdatedAt();
        type = channel.getType();
        name = channel.getName();
        description = channel.getDescription();

        ChannelDTO channelDTO = new ChannelDTO( id, createdAt, updatedAt, type, name, description );

        return channelDTO;
    }

    @Override
    public UpdateChannelDTO toUpdateChannelDTO(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        Instant updatedAt = null;
        ChannelType type = null;
        String name = null;
        String description = null;

        id = channel.getId();
        updatedAt = channel.getUpdatedAt();
        type = channel.getType();
        name = channel.getName();
        description = channel.getDescription();

        UpdateChannelDTO updateChannelDTO = new UpdateChannelDTO( id, updatedAt, type, name, description );

        return updateChannelDTO;
    }
}
