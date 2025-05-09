package com.sprint.mission.discodeit.exception.binary;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FileUploadFailed extends BinaryContentException {
    public FileUploadFailed() {
        super(ErrorCode.FILE_UPLOAD_FAILED);
    }

    public FileUploadFailed(Map<String, Object> details) {
        super(ErrorCode.FILE_UPLOAD_FAILED, details);
    }
}
