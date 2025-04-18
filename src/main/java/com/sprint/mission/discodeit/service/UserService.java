package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest userCreateRequest, BinaryContentCreateRequest profileCreateRequest);

    UserDto searchUser(UUID userId);

    User searchByUsername(String username);

    List<UserDto> findAll();

    User update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentCreateRequest profileCreateRequest);

    void delete(UUID userId);

    UserDto updateOnlineState(UUID userId);
}
