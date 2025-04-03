package com.sprint.mission.discodeit.adapter.inbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.UUID;

public final class ChannelDtoMapper {

  private ChannelDtoMapper() {
  }

  static CreatePublicChannelCommand toCreatePublicChannelCommand(
      PublicChannelCreateRequest requestBody) {
    return new CreatePublicChannelCommand(requestBody.name(), requestBody.description());
  }

  static CreatePrivateChannelCommand toCreatePrivateChannelCommand(
      PrivateChannelCreateRequest requestBody) {
    return new CreatePrivateChannelCommand(requestBody.participantIds());
  }

  static UpdateChannelCommand toUpdateChannelCommand(UUID channelId,
      ChannelUpdateRequest requestBody) {
    return new UpdateChannelCommand(channelId, requestBody.newName(), requestBody.newDescription());
//    return new UpdateChannelCommand(channelId, requestBody.newName(), requestBody.newType());
  }

}
