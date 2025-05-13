package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID readStatusId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, null,
        Map.of(ErrorDetailKey.READ_STATUS_ID.getKey(), readStatusId));
  }
}
