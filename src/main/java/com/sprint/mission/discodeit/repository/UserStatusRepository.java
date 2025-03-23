package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus saveUserStatus(UserStatus userStatus);

    boolean isUserOnline(UUID userId);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    boolean existsByUserId(UUID userId);

    void delete(UUID userId);

}
