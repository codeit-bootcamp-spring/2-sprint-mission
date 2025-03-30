package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.userStatus.UserStatusResult;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResult create(UUID userId);

    UserStatusResult findByStautsId(UUID userStatusId);

    List<UserStatusResult> findAll();

    UserStatusResult findByUserId(UUID userId);

    UserStatusResult updateByStatusId(UUID userStatusId);

    UserStatusResult updateByUserId(UUID userId);

    void delete(UUID userStatusId);
}
