package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentDeleteFailedException extends BinaryContentException {
    public BinaryContentDeleteFailedException(UUID binaryContentId) {
        super(ErrorCode.BINARY_CONTENT_DELETE_FAILED, Map.of("binaryContentId", binaryContentId));
    }
}
