package com.sprint.mission.discodeit.adapter.inbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelDtoMapper {

  ChannelResponse toCreateResponse(ChannelResult channelResult);

  CreatePublicChannelCommand toCreatePublicChannelCommand(PublicChannelCreateRequest requestBody);

  CreatePrivateChannelCommand toCreatePrivateChannelCommand(
      PrivateChannelCreateRequest requestBody);

  UpdateChannelCommand toUpdateChannelCommand(UUID channelId, ChannelUpdateRequest requestBody);
}
