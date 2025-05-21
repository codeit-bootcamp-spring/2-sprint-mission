package com.sprint.mission.discodeit.exception.binary;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class BinaryStatusNotFound extends BinaryContentException{

    public BinaryStatusNotFound() {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND);
    }

    public BinaryStatusNotFound(Map<String, Object> details) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
    }
}
