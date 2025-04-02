package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

    CreateChannelParam toChannelParam(CreatePublicChannelRequestDTO createChannelRequestDTO);
    CreatePublicChannelResponseDTO toChannelResponseDTO(ChannelDTO channelDTO);

    CreatePrivateChannelParam toPrivateChannelParam(CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO);
    CreatePrivateChannelResponseDTO toPrivateChannelResponseDTO(PrivateChannelDTO privateChannelDTO);

    UpdateChannelParam toUpdateChannelParam(UpdateChannelRequestDTO updateChannelRequestDTO);
    UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelDTO updateChannelDTO);

    ChannelDTO toChannelDTO(Channel channel);

    default PrivateChannelDTO toPrivateChannelDTO(List<UUID> userIds, Channel channel) {
        return new PrivateChannelDTO(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                userIds
        );
    }

    UpdateChannelDTO toUpdateChannelDTO(Channel channel);

    default FindChannelDTO toFindChannelDTO(Channel channel, Instant latestMessageTime, List<UUID> userIds) {
        return new FindChannelDTO(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                latestMessageTime,
                userIds
        );
    }
}
