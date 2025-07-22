package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record RoleChangedNotificationEvent(
    UUID receiverId,
    Role oldRole,
    Role newRole,
    String requestId
) {

}