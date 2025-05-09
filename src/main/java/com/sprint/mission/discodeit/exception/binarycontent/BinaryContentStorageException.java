package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class BinaryContentStorageException extends BinaryContentException {
    public BinaryContentStorageException(String fileName) {
        super(ErrorCode.BINARY_CONTENT_STORAGE_ERROR, Map.of("fileName", fileName));
    }

    public BinaryContentStorageException(UUID binaryContentId, String reason) {
        super(ErrorCode.BINARY_CONTENT_STORAGE_ERROR,
                Map.of("binaryContentId", binaryContentId, "reason", reason));
    }
}
