package com.sprint.mission.discodeit.core.status.controller;

import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.dto.ReadStatusUpdateCommand;
import java.util.UUID;

public final class ReadStatusDtoMapper {

  private ReadStatusDtoMapper() {

  }

  public static ReadStatusCreateCommand toCreateReadStatusCommand(
      ReadStatusCreateRequest requestBody) {
    return new ReadStatusCreateCommand(requestBody.userId(), requestBody.channelId(),
        requestBody.lastReadAt());
  }

  public static ReadStatusUpdateCommand toUpdateReadStatusCommand(UUID readStatusId,
      ReadStatusUpdateRequest requestBody) {
    return new ReadStatusUpdateCommand(readStatusId, requestBody.newLastReadAt());
  }

}
