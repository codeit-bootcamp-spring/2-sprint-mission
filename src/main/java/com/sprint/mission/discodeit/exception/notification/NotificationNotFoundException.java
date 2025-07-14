package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {

    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }

    public static NotificationNotFoundException withUserIdAndNotificationId(UUID userId,
        UUID notificationId) {
        NotificationNotFoundException exception = new NotificationNotFoundException();
        exception.addDetail("userId", userId);
        exception.addDetail("notificationId", notificationId);

        return exception;
    }
}
