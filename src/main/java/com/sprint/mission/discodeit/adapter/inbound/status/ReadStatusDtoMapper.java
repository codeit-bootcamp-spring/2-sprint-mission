package com.sprint.mission.discodeit.adapter.inbound.status;

import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import java.util.UUID;

public final class ReadStatusDtoMapper {

  private ReadStatusDtoMapper() {

  }

  static CreateReadStatusCommand toCreateReadStatusCommand(ReadStatusCreateRequest requestBody) {
    return new CreateReadStatusCommand(requestBody.userId(), requestBody.channelId(),
        requestBody.lastReadAt());
  }

  static UpdateReadStatusCommand toUpdateReadStatusCommand(UUID readStatusId,
      ReadStatusUpdateRequest requestBody) {
    return new UpdateReadStatusCommand(readStatusId, requestBody.newLastReadAt());
  }

}
