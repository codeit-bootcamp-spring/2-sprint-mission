package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {
    
    public MessageNotFoundException(Object messageId) {
        super(ErrorCode.MESSAGE_NOT_FOUND, messageId.toString());
    }
} 