package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.service.dto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;
import com.sprint.mission.discodeit.service.dto.UserUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(UserCreateDto userCreateDto);
    UserResponseDto findById(UUID id);
    UserResponseDto findByNickname(String nickname);
    UserResponseDto findByEmail(String email);
    List<UserResponseDto> findAll();
    void updateUser(UserUpdateDto updateDto);
    void deleteUser(UUID id);
}
