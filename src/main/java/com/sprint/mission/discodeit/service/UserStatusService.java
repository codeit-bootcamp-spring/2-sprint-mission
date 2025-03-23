package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserStatusDto;
import com.sprint.mission.discodeit.application.UserStatusesDto;

import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UUID userId);

    UserStatusDto find(UUID userStatusId);

    UserStatusesDto findAll();

    UserStatusDto findByUserId(UUID userId);

    UserStatusDto update(UUID userStatusId);

    UserStatusDto updateByUserId(UUID userId);

    void delete(UUID userStatusId);
}
