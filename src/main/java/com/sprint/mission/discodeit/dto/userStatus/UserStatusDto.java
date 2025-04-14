package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    UUID userid,
    Instant lastActiveAt
) {

  public static UserStatusDto from(UserStatus userStatus) {
    if (userStatus == null) {
      return null;
    }

    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUserid(),
        userStatus.getLastActiveAt()
    );
  }

}
