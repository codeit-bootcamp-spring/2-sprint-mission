package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

  public ReadStatusAlreadyExistsException(List<UUID> channelAndUserId) {
    super(ErrorCode.READ_STATUS_ALREADY_EXISTS, Map.of("channelAndUserId", channelAndUserId));
  }
}
