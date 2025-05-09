package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.service.userstatus.UserStatusResult;

import java.util.UUID;

public interface UserStatusService {
    UserStatusResult create(UserStatusCreateRequest request);

    UserStatusResult getByUserId(UUID userId);

    UserStatusResult updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

    void delete(UUID userStatusId);
}
