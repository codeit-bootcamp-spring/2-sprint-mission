package com.sprint.mission.discodeit.exception.Message;

import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(UUID messageId) {
        super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("messageId",messageId));
    }
}
