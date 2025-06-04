package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageFileProcessingException extends MessageException {
    public MessageFileProcessingException(Map<String, Object> details) {
        super(ErrorCode.FILE_PROCESSING_ERROR, details);
    }
} 