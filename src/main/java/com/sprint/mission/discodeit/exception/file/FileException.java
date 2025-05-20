package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.*;

public class FileException extends DiscodeitException {

    FileException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
