package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;

import java.util.Map;

public class FileProcessingCustomException extends FileException {
    public FileProcessingCustomException() {
        super(ErrorCode.FILE_PROCESSING_ERROR);
    }

}