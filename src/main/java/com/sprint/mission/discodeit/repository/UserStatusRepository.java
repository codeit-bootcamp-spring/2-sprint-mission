package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.model.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public interface UserStatusRepository extends Repository<UserStatus> {
    boolean existsByUserId(UUID userId);
    UserStatus findUserStatusByUserId(UUID userId);
    void updateTimeById(UUID readStatusId, Instant updateTime);
    void updateTimeByUserId(UUID userId, Instant updateTime);
    void updateUserStatusByUserId(UUID id, UserStatusType type);
}
