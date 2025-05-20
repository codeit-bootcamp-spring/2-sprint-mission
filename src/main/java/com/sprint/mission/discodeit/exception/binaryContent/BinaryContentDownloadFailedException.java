package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentDownloadFailedException extends BinaryContentException {
    public BinaryContentDownloadFailedException(UUID binaryContentId) {
        super(ErrorCode.BINARY_CONTENT_DOWNLOAD_FAILED, Map.of("binaryContentId", binaryContentId));
    }
}
