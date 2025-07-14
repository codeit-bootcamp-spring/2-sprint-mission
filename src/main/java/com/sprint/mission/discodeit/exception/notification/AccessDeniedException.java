package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import java.util.UUID;

public class AccessDeniedException extends NotificationException {

    public AccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }

    public static AccessDeniedException withUserIdAndId(UUID userId, UUID notificationId) {
        AccessDeniedException exception = new AccessDeniedException();
        exception.addDetail("userId", userId);
        exception.addDetail("notificationId", notificationId);

        return exception;
    }

}
