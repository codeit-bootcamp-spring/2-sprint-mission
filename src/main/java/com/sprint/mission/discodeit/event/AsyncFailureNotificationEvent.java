package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;

public record AsyncFailureNotificationEvent (
    String title,
    String content,
    NotificationType type
){

}
