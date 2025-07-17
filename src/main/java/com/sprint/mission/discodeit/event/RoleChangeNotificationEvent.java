package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record RoleChangeNotificationEvent (
    String title,
    String content,
    NotificationType type,
    UUID targetId
){

}
