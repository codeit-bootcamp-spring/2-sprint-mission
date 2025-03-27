package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.*;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID createUserStatus(UserStatusCreateRequest userStatusCreateRequest);
    UserStatusFindResponse findUserStatus(UUID id);
    List<UserStatusFindResponse> findAllUserStatus();
    void updateTimeById(UserStatusTimeUpdateRequest userStatusUpdateRequest);
    void updateTimeByUserId(UserStatusTimeUpdateByUserIdRequest userStatusTimeUpdateByUserIdRequest);
    void updateUserStatusByUserId(UUID id, UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest);
    void deleteUserStatus(UUID id);
}
