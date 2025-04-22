package com.sprint.mission.discodeit.adapter.inbound.status;

import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusResponse;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import java.util.UUID;

public final class ReadStatusDtoMapper {

  private ReadStatusDtoMapper() {

  }

  public static ReadStatusResponse toCreateResponse(ReadStatusResult result) {
    return new ReadStatusResponse(result.id(), result.userId(), result.channelId(),
        result.lastReadAt());
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
