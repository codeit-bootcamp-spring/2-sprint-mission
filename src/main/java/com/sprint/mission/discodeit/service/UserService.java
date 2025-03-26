package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserDto;
import com.sprint.mission.discodeit.dto.ReadUserDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(CreateUserDto dto);
    ReadUserDto read(UUID userKey);
    List<ReadUserDto> readAll();
    UpdateUserResponseDto update(UpdateUserRequestDto dto);
    void delete(UUID userKey);
}
