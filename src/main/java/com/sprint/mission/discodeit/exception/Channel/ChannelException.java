package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.Basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.Basic.ErrorCode;

import java.util.Map;

public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
