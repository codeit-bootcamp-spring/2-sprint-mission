package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JCFUserStatusRepository {
    void save(UUID userId);
    Optional<UserStatus> findByUser(UUID userUUID);
    List<UserStatus> findAll();
    void update(UUID userId);
    void delete(UUID userId);
}
