package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.common.exception.DiscodeitException;
import com.sprint.mission.common.exception.ErrorCode;

import java.util.Map;

public class ChannelException extends DiscodeitException {

    public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

}
