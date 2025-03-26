package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDTO;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(CreateUserStatusDTO dto);

    UserStatus findById(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus updateUserStatus(UUID userStatusId, UpdateUserStatusDTO dto);

    UserStatus updateByUserId(UUID userId, UpdateUserStatusDTO dto);

    void deleteUserStatus(UUID userStatusId);
}
