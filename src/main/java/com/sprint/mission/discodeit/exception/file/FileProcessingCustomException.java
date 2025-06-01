package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileProcessingCustomException extends FileException {

    public FileProcessingCustomException(Map<String, Object> details) {
        super(ErrorCode.FILE_PROCESSING_ERROR, details);
    }
} 