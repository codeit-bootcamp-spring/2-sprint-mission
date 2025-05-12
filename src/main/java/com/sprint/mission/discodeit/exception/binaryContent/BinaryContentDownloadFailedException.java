package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import java.util.Map;

public class BinaryContentDownloadFailedException extends BinaryContentException {
    public BinaryContentDownloadFailedException() {
        super(ErrorCode.BINARY_CONTENT_DOWNLOAD_FAILED, Map.of());
    }
}
