package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save();

    void addUserStatus(UserStatus userStatus);

    Optional<UserStatus> findUserStatusById(UUID userId);

    List<UserStatus> findAllUserStatus();

    void deleteUserStatusById(UUID userId);

    boolean existsUserStatusById(UUID userId);

}
