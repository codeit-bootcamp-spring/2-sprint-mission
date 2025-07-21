package com.sprint.mission.discodeit.dto.event;

import com.sprint.mission.discodeit.constant.Role;
import com.sprint.mission.discodeit.dto.user.UserDto;

public record NotificationRoleUpdateEvent(
    UserDto userDto, Role previousRole
) {
}
