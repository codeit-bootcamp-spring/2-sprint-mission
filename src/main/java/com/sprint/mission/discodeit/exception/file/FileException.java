package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.*;

public class FileException extends DiscodeitException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }

}
