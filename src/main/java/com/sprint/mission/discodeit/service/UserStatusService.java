package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.UserStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UUID createUserStatus(UserStatusCreateDto createDto);
    UserStatusResponseDto findById(UUID id);
    List<UserStatusResponseDto> findAll();
    UserStatusResponseDto findByUserId(UUID userId);
    void updateUserStatus(UserStatusUpdateDto updateDto);
    void updateByUserId(UUID userId);
    void deleteUserStatus(UUID id);
}
