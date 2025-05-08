package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyException extends ReadStatusException {

    public ReadStatusAlreadyException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_READ_STATUS, details);
    }

    public static ReadStatusAlreadyException forUserIdAndChannelId(String userId,
        String channelId) {
        return new ReadStatusAlreadyException(Map.of("userId", userId, "channelId", channelId));
    }
}
