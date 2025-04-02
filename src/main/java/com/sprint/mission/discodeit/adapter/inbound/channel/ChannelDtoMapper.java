package com.sprint.mission.discodeit.adapter.inbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.UUID;

public final class ChannelDtoMapper {

  private ChannelDtoMapper() {
  }

  static CreatePublicChannelCommand toCreatePublicChannelCommand(UUID userId,
      PublicChannelCreateRequest requestBody) {
    return new CreatePublicChannelCommand(userId, requestBody.name());
  }

  static CreatePrivateChannelCommand toCreatePrivateChannelCommand(
      UUID userId,
      PrivateChannelCreateRequest requestBody) {
    return new CreatePrivateChannelCommand(userId, requestBody.participantIds());
  }

  static UpdateChannelCommand toUpdateChannelCommand(UUID channelId,
      ChannelUpdateRequest requestBody) {
    return new UpdateChannelCommand(channelId, requestBody.newName(), requestBody.newType());
  }

}
