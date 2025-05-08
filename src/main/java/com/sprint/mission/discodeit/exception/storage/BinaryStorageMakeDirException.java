package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryStorageMakeDirException extends BinaryStorageException {

    public BinaryStorageMakeDirException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_STORAGE_MAKE_DIR_ERROR, details, cause);
    }

    public static BinaryStorageMakeDirException from(String dirPath, Throwable cause) {
        return new BinaryStorageMakeDirException(Map.of("dirPath", dirPath), cause);
    }
}
