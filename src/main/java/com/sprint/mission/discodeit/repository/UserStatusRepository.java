package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UUID createUserStatus(UserStatus userStatus);
    UserStatus findById(UUID id);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    void updateUserStatus(UUID id, UUID userId, Instant lastActiveAt);
    void updateByUserId(UUID userId, Instant now);
    void deleteUserStatus(UUID id);
}
