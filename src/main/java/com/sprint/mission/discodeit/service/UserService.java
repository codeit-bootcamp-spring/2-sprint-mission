package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(UserCreateDto userCreateDto);
    UserDto findById(UUID id);
    List<UserDto> findAll();
    void update(UserUpdateDto userUpdateDto);
    void delete(UUID id);
}
