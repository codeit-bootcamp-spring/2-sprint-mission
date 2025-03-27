package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

    CreateChannelParam toChannelParam(CreatePublicChannelRequestDTO createChannelRequestDTO);
    CreatePublicChannelResponseDTO toChannelResponseDTO(ChannelDTO channelDTO);

    CreatePrivateChannelParam toPrivateChannelParam(CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO);
    CreatePrivateChannelResponseDTO toPrivateChannelResponseDTO(PrivateChannelDTO privateChannelDTO);

    UpdateChannelParam toUpdateChannelParam(UpdateChannelRequestDTO updateChannelRequestDTO);
    UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelDTO updateChannelDTO);



}
