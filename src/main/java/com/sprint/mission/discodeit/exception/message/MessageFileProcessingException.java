package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageFileProcessingException extends MessageException {


    public MessageFileProcessingException() {
        super(ErrorCode.FILE_PROCESSING_ERROR);
    }
} 