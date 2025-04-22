package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.*;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

  CreatePublicChannelCommand toCreatePublicChannelCommand(
      CreatePublicChannelRequestDTO createPublicChannelRequestDTO);

  CreatePublicChannelResponseDTO toCreatePublicChannelResponseDTO(
      CreatePublicChannelResult createPublicChannelResult);

  CreatePrivateChannelCommand toCreatePrivateChannelCommand(
      CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO);

  CreatePrivateChannelResponseDTO toCreatePrivateChannelResponseDTO(
      CreatePrivateChannelResult createPrivateChannelResult);

  UpdateChannelCommand toUpdateChannelCommand(UpdateChannelRequestDTO updateChannelRequestDTO);

  UpdateChannelResponseDTO toUpdateChannelResponseDTO(UpdateChannelResult updateChannelResult);

  UpdateChannelResult toUpdateChannelResult(Channel channel, Instant lastMessageAt);

  FindChannelResponseDTO toFindChannelResponseDTO(FindChannelResult findChannelResult);

  FindChannelResult toFindChannelResult(Channel channel, Instant lastMessageAt,
      List<FindUserResult> participants);

  CreatePrivateChannelResult toCreatePrivateChannelResult(Channel channel, Instant lastMessageAt,
      List<FindUserResult> participants);

  CreatePublicChannelResult toCreatePublicChannelResult(Channel channel);
}
