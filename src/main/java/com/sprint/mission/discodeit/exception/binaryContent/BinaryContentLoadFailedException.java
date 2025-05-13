package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentLoadFailedException extends BinaryContentException {
    public BinaryContentLoadFailedException(UUID binaryContentId) {
        super(ErrorCode.BINARY_CONTENT_LOAD_FAILED, Map.of("binaryContentId", binaryContentId));
    }
}
