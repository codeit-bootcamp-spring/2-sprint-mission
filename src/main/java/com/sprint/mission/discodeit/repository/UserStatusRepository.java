package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    void delete(UUID userStatusKey);
    List<UserStatus> findAll();
    UserStatus findByUserKey(UUID userKey);
    UserStatus findByKey(UUID userStatusKey);
}
