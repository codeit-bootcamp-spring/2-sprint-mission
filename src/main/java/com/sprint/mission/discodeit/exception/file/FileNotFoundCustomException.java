package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class FileNotFoundCustomException extends FileException {

    public FileNotFoundCustomException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }

} 