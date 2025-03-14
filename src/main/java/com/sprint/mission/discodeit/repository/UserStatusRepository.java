package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UUID userUUID);
    Optional<UserStatus> findByUser(UUID userUUID);
    List<UserStatus> findAll();
    void update(UUID userUUID);
    void delete(UUID userUUID);
}
