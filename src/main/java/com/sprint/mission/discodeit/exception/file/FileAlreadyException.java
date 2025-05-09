package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.*;

public class FileAlreadyException extends FileException {

    public FileAlreadyException(Instant timestamp, ErrorCode errorCode,
        Map<String, Object> details) {
        super(timestamp, errorCode, details);
    }

}
