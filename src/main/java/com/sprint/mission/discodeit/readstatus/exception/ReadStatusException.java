package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class ReadStatusException extends DiscodeitException {

    public ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
