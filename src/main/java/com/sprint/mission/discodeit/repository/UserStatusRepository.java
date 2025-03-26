package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    Optional<UserStatus> findByUserId(UUID userId);
    UserStatus save(UserStatus userStatus);
    void deleteByUserId(UUID userId);
    Optional<UserStatus> findById(UUID id);
    List<UserStatus> findAll();
    boolean existsById(UUID id);
    boolean existsByUserId(UUID userId);
    void deleteById(UUID id);
}
