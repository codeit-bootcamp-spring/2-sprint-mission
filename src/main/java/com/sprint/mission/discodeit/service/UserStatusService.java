package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto createUserStatus(UserStatusCreateRequest request);

    UserStatusDto find(UUID userStatusId);

    List<UserStatusDto> findAll();

    UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request);

    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

    void deleteUserStatus(UUID userStatusId);
}
