package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentDeleteFailedException extends BinaryContentException {
    public BinaryContentDeleteFailedException() {
        super(ErrorCode.BINARY_CONTENT_DELETE_FAILED, Map.of());
    }
}
