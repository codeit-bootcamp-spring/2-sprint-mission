package com.sprint.mission.discodeit.domain.binarycontent.exception;

import java.util.Map;

import static com.sprint.mission.discodeit.common.exception.ErrorCode.ERROR_BINARY_CONTENT_NOT_FOUND;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(Map<String, Object> details) {
        super(ERROR_BINARY_CONTENT_NOT_FOUND, details);
    }

}
