package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toResponse(UserStatus status) {
    return new UserStatusDto(
        status.getId(),
        status.getUser().getId(),
        status.getLastActiveAt()
    );
  }
}