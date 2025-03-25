package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest requestDto);
    UserStatus find(UUID id);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequest requestDto);
    UserStatus updateByUserId(UUID userId);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
