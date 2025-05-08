package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

    public MessageNotFoundException(Map<String, Object> details) {
        super(ErrorCode.MESSAGE_NOT_FOUND, details);
    }

    public static MessageNotFoundException forId(String messageId) {
        return new MessageNotFoundException(Map.of("messageId", messageId));
    }
}
