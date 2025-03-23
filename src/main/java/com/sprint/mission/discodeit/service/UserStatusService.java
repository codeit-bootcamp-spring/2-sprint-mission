package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserStatusDto;

import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UUID userId);
}
