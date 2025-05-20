package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentSaveFailedException extends BinaryContentException {
    public BinaryContentSaveFailedException(UUID binaryContentId) {
        super(ErrorCode.BINARY_CONTENT_SAVE_FAILED, Map.of("binaryContentId", binaryContentId));
    }
}
