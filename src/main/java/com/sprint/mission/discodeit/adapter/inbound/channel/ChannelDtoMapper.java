package com.sprint.mission.discodeit.adapter.inbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.UUID;

public final class ChannelDtoMapper {

  private ChannelDtoMapper() {

  }

  public static ChannelResponse toCreateResponse(ChannelResult result) {
    return new ChannelResponse(result.id(), result.type(), result.name(), result.description(),
        result.participants().stream().map(
            UserDtoMapper::toCreateResponse).toList(),
        result.lastMessageAt());
  }

  public static CreatePublicChannelCommand toCreatePublicChannelCommand(
      PublicChannelCreateRequest requestBody) {
    return new CreatePublicChannelCommand(requestBody.name(), requestBody.description());
  }

  public static CreatePrivateChannelCommand toCreatePrivateChannelCommand(
      PrivateChannelCreateRequest requestBody) {
    return new CreatePrivateChannelCommand(requestBody.participantIds());
  }

  public static UpdateChannelCommand toUpdateChannelCommand(UUID channelId,
      ChannelUpdateRequest requestBody) {
    return new UpdateChannelCommand(channelId, requestBody.newName(), requestBody.newDescription());
  }
}
