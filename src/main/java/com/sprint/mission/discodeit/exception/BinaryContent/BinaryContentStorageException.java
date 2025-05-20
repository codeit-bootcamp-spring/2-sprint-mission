package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class BinaryContentStorageException extends BinaryContentException{
    public BinaryContentStorageException(UUID binaryContentId) {
        super(ErrorCode.FILE_STORAGE_ERROR, Map.of("binaryContentId", binaryContentId));
    }
}
