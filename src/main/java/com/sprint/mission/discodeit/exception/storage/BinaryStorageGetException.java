package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryStorageGetException extends BinaryStorageException {

    public BinaryStorageGetException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_STORAGE_GET_ERROR, details, cause);
    }

    public static BinaryStorageGetException forId(String binaryContentId, Throwable cause) {
        return new BinaryStorageGetException(Map.of("binaryContentId", binaryContentId), cause);
    }
}
