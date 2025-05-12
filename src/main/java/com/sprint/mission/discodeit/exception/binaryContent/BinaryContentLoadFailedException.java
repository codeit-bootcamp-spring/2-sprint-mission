package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentLoadFailedException extends BinaryContentException {
    public BinaryContentLoadFailedException() {
        super(ErrorCode.BINARY_CONTENT_LOAD_FAILED, Map.of());
    }
}
