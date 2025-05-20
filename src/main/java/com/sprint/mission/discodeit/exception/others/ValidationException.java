package com.sprint.mission.discodeit.exception.others;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ValidationException extends DiscodeitException {
    public ValidationException(Map<String, Object> validationErrors) {
        super(ErrorCode.VALIDATION_ERROR, validationErrors);
    }
}
