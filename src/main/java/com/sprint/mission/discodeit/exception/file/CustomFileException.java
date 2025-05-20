package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class CustomFileException extends DiscodeitException {
    public CustomFileException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public CustomFileException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, details);
        // DiscodeitException does not have a constructor to directly set a custom message like this.
        // The message is typically derived from ErrorCode or overridden in subclasses.
        // For now, we'll rely on the ErrorCode's message. If custom messages are needed per instance,
        // a different approach for message handling in DiscodeitException or its subclasses would be required.
    }
} 