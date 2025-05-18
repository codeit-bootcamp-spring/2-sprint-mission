package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class BinaryContentException extends DiscodeitException {

    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
