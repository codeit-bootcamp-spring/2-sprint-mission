package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class PrivateChannelUpdate extends ChannelException{

    public PrivateChannelUpdate() {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
    }

    public PrivateChannelUpdate(Map<String, Object> details) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
    }
}
