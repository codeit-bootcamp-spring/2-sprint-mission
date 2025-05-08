package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryStoragePutException extends BinaryStorageException {

    public BinaryStoragePutException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_STORAGE_PUT_ERROR, details, cause);
    }

    public static BinaryStoragePutException forId(String binaryContentId, Throwable cause) {
        return new BinaryStoragePutException(Map.of("binaryContentId", binaryContentId), cause);
    }
}
