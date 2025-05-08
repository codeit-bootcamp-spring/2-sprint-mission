package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.constant.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class ReadStatusException extends DiscodeitException {

    public ReadStatusException(ErrorCode errorCode,
        Map<String, Object> details) {
        super(errorCode, details);
    }
}
