package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateReadStatusException extends ReadStatusException {

    public DuplicateReadStatusException() {
        super(ErrorCode.DUPLICATE_READ_STATUS);
    }

    public DuplicateReadStatusException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_READ_STATUS, details);
    }
}
