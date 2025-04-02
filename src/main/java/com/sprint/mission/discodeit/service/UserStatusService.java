package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatus userStatus);
    UserStatus findById(UUID id);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    UserStatus updateByUserId(UUID userId);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
