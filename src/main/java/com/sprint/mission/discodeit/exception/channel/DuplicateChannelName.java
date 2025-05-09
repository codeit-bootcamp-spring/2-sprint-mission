package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateChannelName extends ChannelException {
    public DuplicateChannelName() {
        super(ErrorCode.DUPLICATE_CHANNEL_NAME);
    }

    public DuplicateChannelName(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_CHANNEL_NAME, details);
    }
}
