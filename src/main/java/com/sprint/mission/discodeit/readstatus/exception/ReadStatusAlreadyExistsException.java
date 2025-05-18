package com.sprint.mission.discodeit.readstatus.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_READ_STATUS_ALREADY_EXISTS;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

    public ReadStatusAlreadyExistsException(Map<String, Object> details) {
        super(ERROR_READ_STATUS_ALREADY_EXISTS, details);
    }

}
