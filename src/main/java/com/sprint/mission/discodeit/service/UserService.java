package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(UserCreateDto userCreateDto);
    UserDto findById(UUID id);
    List<UserDto> findAll();
    void update(UserUpdateDto userUpdateDto) throws IOException;
    void delete(UUID id);
}
