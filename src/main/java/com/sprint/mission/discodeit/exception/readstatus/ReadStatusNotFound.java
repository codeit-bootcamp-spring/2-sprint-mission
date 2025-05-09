package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ReadStatusNotFound extends ReadStatusException{
    public ReadStatusNotFound() {
        super(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    public ReadStatusNotFound(Map<String, Object> details) {
        super(ErrorCode.READ_STATUS_NOT_FOUND, details);
    }
}
