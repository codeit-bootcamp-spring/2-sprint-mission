package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
// import java.time.Instant; // No longer needed in constructor
import java.util.*;

public class FileAlreadyException extends FileException {

    public FileAlreadyException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.FILE_PROCESSING_ERROR, details);
    }

}
