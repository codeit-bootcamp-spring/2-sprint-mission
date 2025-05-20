package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryStorageDownloadException extends BinaryStorageException {

    public BinaryStorageDownloadException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_STORAGE_DOWNLOAD_ERROR, details, cause);
    }

    public static BinaryStorageDownloadException forId(String binaryContentId, Throwable cause) {
        return new BinaryStorageDownloadException(Map.of("binaryContentId", binaryContentId),
            cause);
    }
}
