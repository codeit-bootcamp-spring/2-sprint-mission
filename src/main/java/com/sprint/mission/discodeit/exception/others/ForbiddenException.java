package com.sprint.mission.discodeit.exception.others;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ForbiddenException extends DiscodeitException {
    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String resource, String action) {
        super(ErrorCode.FORBIDDEN, Map.of("resource", resource, "action", action));
    }
}
