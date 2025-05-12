package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(UUID messageId) {
        super(ErrorCode.USER_NOT_FOUND, Map.of("messageId", messageId));
    }
}
