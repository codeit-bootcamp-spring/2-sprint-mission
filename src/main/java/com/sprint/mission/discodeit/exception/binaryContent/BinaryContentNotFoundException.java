package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(Map<String, Object> details) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
    }

    public static BinaryContentNotFoundException forId(String binaryContentId) {
        return new BinaryContentNotFoundException(Map.of("binaryContentId", binaryContentId));
    }
}
