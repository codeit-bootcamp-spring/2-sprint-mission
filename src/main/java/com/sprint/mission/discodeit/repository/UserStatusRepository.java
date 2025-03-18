package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository extends Repository<UserStatus> {
    UserStatus findUserStatusIDByUserId(UUID userId);
}
