package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    Optional<UserStatus> findByUserId(UUID userId);
    void save(UserStatus userStatus);
    void updateLastActiveAt(UUID userId, Instant newLastActiveAt);
    void deleteById(UUID userId);
    boolean isUserOnline(UUID userId);
}
