package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID id);

    void delete(UUID id);
}
