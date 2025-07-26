package com.sprint.mission.discodeit.domain.auth.event;

import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import java.util.UUID;

public record UserRoleChangedEvent(
    UUID userId,
    Role previousRole,
    Role updatedRole
) {

  public static UserRoleChangedEvent of(Role previousRole, User updatedUser) {
    return new UserRoleChangedEvent(
        updatedUser.getId(),
        previousRole,
        updatedUser.getRole()
    );
  }

}
