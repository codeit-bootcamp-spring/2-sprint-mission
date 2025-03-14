package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreateResponseDto create(UserCreateRequestDto userCreateRequestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    User update(UUID userId, String newUsername, String newEmail, String newPassword, UUID profileId);
    void delete(UUID userId);
}
