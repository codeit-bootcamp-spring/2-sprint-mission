package com.sprint.mission.discodeit.adapter.inbound.user;

import com.sprint.mission.discodeit.adapter.inbound.user.response.UserStatusResponse;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;


public final class UserStatusDtoMapper {

  private UserStatusDtoMapper() {

  }

  public static UserStatusResponse toCreateResponse(UserStatusResult result) {
    return new UserStatusResponse(result.id(), result.userId(), result.lastActiveAt());
  }
}
