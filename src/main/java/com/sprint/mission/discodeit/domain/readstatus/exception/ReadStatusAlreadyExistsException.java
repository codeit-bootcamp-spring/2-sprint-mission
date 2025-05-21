package com.sprint.mission.discodeit.domain.readstatus.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_READ_STATUS_ALREADY_EXISTS;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

    public ReadStatusAlreadyExistsException(Map<String, Object> details) {
        super(ERROR_READ_STATUS_ALREADY_EXISTS, details);
    }

}
