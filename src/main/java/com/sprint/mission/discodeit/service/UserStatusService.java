package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusFindResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID createUserStatus(UserStatusCreateRequest userStatusCreateRequest);
    UserStatusFindResponse findUserStatus(UUID id);
    List<UserStatusFindResponse> findAllUserStatus();
    void updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest);
    void updateUserStatusByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    void deleteUserStatus(UUID id);
}
