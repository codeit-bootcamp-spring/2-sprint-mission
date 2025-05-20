package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.Basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;

public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
