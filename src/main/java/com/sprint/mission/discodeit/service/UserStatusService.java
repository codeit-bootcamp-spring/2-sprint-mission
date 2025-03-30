package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatusService.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusService.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest request);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequest request);
    UserStatus updateByUserId(UUID id);
    void delete(UUID id);
}
