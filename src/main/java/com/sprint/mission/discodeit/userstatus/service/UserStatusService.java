package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;

import java.util.UUID;

public interface UserStatusService {
    UserStatusResult create(UserStatusCreateRequest request);

    UserStatusResult getByUserId(UUID userId);

    UserStatusResult updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

    void delete(UUID userStatusId);
}
