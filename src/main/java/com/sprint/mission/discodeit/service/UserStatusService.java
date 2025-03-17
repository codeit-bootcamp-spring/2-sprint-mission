package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDto userStatusCreateRequestDto);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequestDto userStatusUpdateRequestDto);
    UserStatus updateByUserId(UUID userId);
    void delete(UUID id);
    void deleteByUserId(UUID userId);
}
