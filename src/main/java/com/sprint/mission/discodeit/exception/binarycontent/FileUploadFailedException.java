package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileUploadFailedException extends FileException {

    public FileUploadFailedException() {
        super(ErrorCode.FILE_UPLOAD_FAILED);
    }

    public FileUploadFailedException(Map<String, Object> details) {
        super(ErrorCode.FILE_UPLOAD_FAILED, details);
    }
}
