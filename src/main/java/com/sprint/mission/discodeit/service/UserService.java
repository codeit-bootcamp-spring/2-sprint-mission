package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.*;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserCreateResponseDto create(UserCreateRequestDto requestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    UserUpdateResponseDto update(UserUpdateRequestDto requestDto);
    void delete(UUID userId);
}
