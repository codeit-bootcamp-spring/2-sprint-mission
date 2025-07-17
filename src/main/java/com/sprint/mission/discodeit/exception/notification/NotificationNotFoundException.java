package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class NotificationNotFoundException extends NotificationException {

    public NotificationNotFoundException(
        Map<String, Object> details) {
        super(ErrorCode.NOTIFICATION_NOT_FOUND_ERROR, details);
    }

    public static NotificationNotFoundException forId(String NotificationId) {
        return new NotificationNotFoundException(Map.of("NotificationId", NotificationId));
    }
}
