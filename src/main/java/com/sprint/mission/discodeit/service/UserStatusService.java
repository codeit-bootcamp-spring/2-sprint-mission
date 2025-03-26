package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.user.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDto requestDto);
    UserStatus find(UUID id);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequestDto requestDto);
    UserStatus updateByUserId(UUID userId);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
