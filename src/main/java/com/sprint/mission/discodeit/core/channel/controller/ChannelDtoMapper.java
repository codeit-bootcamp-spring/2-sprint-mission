package com.sprint.mission.discodeit.core.channel.controller;

import com.sprint.mission.discodeit.core.channel.controller.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import java.util.UUID;

public final class ChannelDtoMapper {

  private ChannelDtoMapper() {

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
