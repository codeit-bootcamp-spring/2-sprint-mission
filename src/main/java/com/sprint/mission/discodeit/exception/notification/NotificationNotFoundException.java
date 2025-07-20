package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;


public class NotificationNotFoundException extends DiscodeitException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
} 