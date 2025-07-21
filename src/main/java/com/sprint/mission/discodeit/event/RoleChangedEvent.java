package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Role;

public record RoleChangedEvent(
    Role previousRole,
    UserDto userDto
) {

}
