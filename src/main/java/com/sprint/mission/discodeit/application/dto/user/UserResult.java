package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserResult(UUID id, String name, String email, UUID profileId, boolean isLogin) {

  public static UserResult fromEntity(User user, boolean isLogin) {
    return new UserResult(user.getId(), user.getName(), user.getEmail(), user.getProfileId(),
        isLogin);
  }
}
