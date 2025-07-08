package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileProcessingCustomException extends FileException {

    public FileProcessingCustomException() {
        super(ErrorCode.FILE_PROCESSING_ERROR);
    }

}