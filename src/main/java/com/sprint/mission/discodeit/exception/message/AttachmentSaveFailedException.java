package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AttachmentSaveFailedException extends MessageException {

    public AttachmentSaveFailedException() {
        super(ErrorCode.ATTACHMENT_SAVE_FAILED);
    }

    public AttachmentSaveFailedException(Map<String, Object> details) {
        super(ErrorCode.ATTACHMENT_SAVE_FAILED, details);
    }
}
