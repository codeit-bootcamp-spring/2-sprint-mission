package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentSaveFailedException extends BinaryContentException {
    public BinaryContentSaveFailedException() {
        super(ErrorCode.BINARY_CONTENT_SAVE_FAILED, Map.of());
    }
}
