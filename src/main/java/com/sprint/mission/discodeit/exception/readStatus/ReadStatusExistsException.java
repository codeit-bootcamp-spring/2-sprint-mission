package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusExistsException extends ReadStatusException {
    public ReadStatusExistsException(UUID userId, UUID channelId) {
        super(ErrorCode.READ_STATUS_ALREADY_EXISTS,
                Map.of("userId", userId, "channelId", channelId)
        );
    }
}
