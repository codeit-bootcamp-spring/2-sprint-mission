package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelNotUpdate extends ChannelException {

    public PrivateChannelNotUpdate(Map<String, Object> details) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED, details);
    }

}
