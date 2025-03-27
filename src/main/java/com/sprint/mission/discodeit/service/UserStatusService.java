package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserStatusDTO;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusDTO createUserStatusDTO);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    void update(UpdateUserStatusDTO updateUserStatusDTO);
    UserStatus updateByUserId(UUID userId, Instant lastModified);
    void delete(UUID id);
}
