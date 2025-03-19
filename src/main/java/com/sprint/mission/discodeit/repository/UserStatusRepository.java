package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository extends Repository<UserStatus> {
    boolean existsByUserId(UUID userId);
    UserStatus findUserStatusIDByUserId(UUID userId);
}
