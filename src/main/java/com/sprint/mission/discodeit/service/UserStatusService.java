package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatusCreateRequest dto);

    UserStatus findById(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request);

    UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

    void deleteUserStatus(UUID userStatusId);
}
