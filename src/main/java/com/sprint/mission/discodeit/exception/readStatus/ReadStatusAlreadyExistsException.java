package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

  public ReadStatusAlreadyExistsException(UUID userId, UUID channelId, String message) {
    super(
        ErrorCode.READ_STATUS_ALREADY_EXIST,
        message,
        Map.of(
            ErrorDetailKey.USER_ID.getKey(), userId,
            ErrorDetailKey.CHANNEL_ID.getKey(), channelId
        )
    );
  }
}
