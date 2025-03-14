package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreateResponseDto create(UserCreateRequestDto userCreateRequestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto);
    void delete(UUID userId);
}
