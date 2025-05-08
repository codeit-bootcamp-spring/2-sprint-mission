package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryMetadataUploadException extends BinaryContentException {

    public BinaryMetadataUploadException(Map<String, Object> details, Throwable cause) {
        super(ErrorCode.BINARY_METADATA_UPLOAD_ERROR, details, cause);
    }
}
