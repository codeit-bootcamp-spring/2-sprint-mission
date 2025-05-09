package com.sprint.mission.discodeit.exception.others;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class BadRequestException extends DiscodeitException {
    public BadRequestException(String reason) {
        super(ErrorCode.BAD_REQUEST, Map.of("reason", reason));
    }
}
