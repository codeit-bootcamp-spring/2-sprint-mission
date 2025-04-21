package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusMapper {
    UserStatusDto toDto(UserStatus userStatus);
}
