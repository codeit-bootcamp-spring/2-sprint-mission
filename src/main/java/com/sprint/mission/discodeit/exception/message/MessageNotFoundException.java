package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorDetailKey;
import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(UUID messageId) {
    super(ErrorCode.MESSAGE_NOT_FOUND, null, Map.of(ErrorDetailKey.MESSAGE_ID.getKey(), messageId));
  }
}
