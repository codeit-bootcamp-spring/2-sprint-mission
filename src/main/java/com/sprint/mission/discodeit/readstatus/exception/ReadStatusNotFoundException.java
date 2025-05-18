package com.sprint.mission.discodeit.readstatus.exception;

import java.util.Map;

import static com.sprint.mission.common.exception.ErrorCode.ERROR_READ_STATUS_NOT_FOUND;

public class ReadStatusNotFoundException extends ReadStatusException {

    public ReadStatusNotFoundException(Map<String, Object> details) {
        super(ERROR_READ_STATUS_NOT_FOUND, details);
    }

}
