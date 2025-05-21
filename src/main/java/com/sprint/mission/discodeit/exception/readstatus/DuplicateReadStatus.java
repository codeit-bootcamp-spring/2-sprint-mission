package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateReadStatus extends ReadStatusException{
    public DuplicateReadStatus() {
        super(ErrorCode.DUPLICATE_READ_STATUS);
    }

    public DuplicateReadStatus(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_READ_STATUS, details);
    }
}
