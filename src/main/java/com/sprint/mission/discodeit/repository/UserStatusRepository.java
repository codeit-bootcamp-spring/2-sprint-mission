package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.userDto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus findByUserId(UUID userId);
    void save(UserStatus userStatus);
    void deleteByUserId(UUID userId);
    List<UserResponse> findAll();
}