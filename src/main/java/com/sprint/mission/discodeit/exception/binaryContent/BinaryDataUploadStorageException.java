package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryDataUploadStorageException extends BinaryContentException {

    public BinaryDataUploadStorageException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_DATA_UPLOAD_STORAGE_ERROR, details, cause);
    }
}
