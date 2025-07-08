package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {
    
    public ChannelNotFoundException(Object channelId) {
        super(ErrorCode.CHANNEL_NOT_FOUND, channelId.toString());
    }
} 