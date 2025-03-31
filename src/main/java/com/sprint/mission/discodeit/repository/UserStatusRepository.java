package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    UserStatus findByUserId(UUID id);
    UserStatus findById(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UUID id);
    void delete(UUID id);
}
