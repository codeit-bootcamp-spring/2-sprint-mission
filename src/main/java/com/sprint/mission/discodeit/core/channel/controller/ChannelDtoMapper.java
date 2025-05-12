package com.sprint.mission.discodeit.core.channel.controller;

import com.sprint.mission.discodeit.core.channel.controller.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;
import java.util.UUID;

public final class ChannelDtoMapper {

  private ChannelDtoMapper() {

  }

  public static PublicChannelCreateCommand toCreatePublicChannelCommand(
      PublicChannelCreateRequest requestBody) {
    return new PublicChannelCreateCommand(requestBody.name(), requestBody.description());
  }

  public static PrivateChannelCreateCommand toCreatePrivateChannelCommand(
      PrivateChannelCreateRequest requestBody) {
    return new PrivateChannelCreateCommand(requestBody.participantIds());
  }

  public static ChannelUpdateCommand toUpdateChannelCommand(UUID channelId,
      ChannelUpdateRequest requestBody) {
    return new ChannelUpdateCommand(channelId, requestBody.newName(), requestBody.newDescription());
  }
}
