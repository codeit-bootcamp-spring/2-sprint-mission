package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.*;

public class PrivateChannelNotUpdate extends ChannelException {

    public PrivateChannelNotUpdate(Instant timestamp, ErrorCode errorCode,
        Map<String, Object> details) {
        super(errorCode, details);
    }

}
