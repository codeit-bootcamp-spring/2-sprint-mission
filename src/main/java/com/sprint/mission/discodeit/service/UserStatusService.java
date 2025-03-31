package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.UserStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatusCreateDto createDto);
    UserStatusResponseDto findById(UUID id);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto findByUserId(UUID userId);
    void updateUserStatus(UserStatusUpdateDto updateDto);
    UserStatus updateByUserId(UUID userId);
    void deleteUserStatus(UUID id);
}
