package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusResponse toResponse(UserStatus status) {
    return new UserStatusResponse(
        status.getId(),
        status.getUser().getId(),
        status.getLastActiveAt()
    );
  }
}