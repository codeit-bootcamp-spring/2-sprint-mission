package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResult create(UUID userId);

    UserStatusResult getByStatusId(UUID userStatusId);

    List<UserStatusResult> getAll();

    UserStatusResult getByUserId(UUID userId);

    UserStatusResult updateByStatusId(UUID userStatusId);

    UserStatusResult updateByUserId(UUID userId);

    void delete(UUID userStatusId);
}
