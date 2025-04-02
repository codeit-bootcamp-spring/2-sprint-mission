package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.DTO.UserStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusDto dto);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UUID id, UpdateUserStatusDto dto);
    UserStatus updateByUserId(UUID userId, UpdateUserStatusDto dto);
    void delete(UUID id);
}
