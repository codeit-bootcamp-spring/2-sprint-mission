package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;

import java.util.UUID;

public interface UserStatusService {
    UserStatusResult create(UserStatusCreateRequest request);

    UserStatusResult getByUserId(UUID userId);

    UserStatusResult updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

    void delete(UUID userStatusId);
}
