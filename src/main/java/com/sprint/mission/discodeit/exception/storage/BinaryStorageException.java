package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.constant.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class BinaryStorageException extends DiscodeitException {

    public BinaryStorageException(ErrorCode errorCode,
        Map<String, Object> details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
