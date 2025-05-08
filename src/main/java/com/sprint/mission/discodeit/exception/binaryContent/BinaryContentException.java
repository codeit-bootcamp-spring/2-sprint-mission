package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.constant.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details,
        Throwable cause) {
        super(errorCode, details, cause);
    }
}
