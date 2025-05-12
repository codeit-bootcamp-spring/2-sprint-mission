package com.sprint.mission.discodeit.core.status.controller;

import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import java.util.UUID;

public final class ReadStatusDtoMapper {

  private ReadStatusDtoMapper() {

  }

  public static CreateReadStatusCommand toCreateReadStatusCommand(
      ReadStatusCreateRequest requestBody) {
    return new CreateReadStatusCommand(requestBody.userId(), requestBody.channelId(),
        requestBody.lastReadAt());
  }

  public static UpdateReadStatusCommand toUpdateReadStatusCommand(UUID readStatusId,
      ReadStatusUpdateRequest requestBody) {
    return new UpdateReadStatusCommand(readStatusId, requestBody.newLastReadAt());
  }

}
