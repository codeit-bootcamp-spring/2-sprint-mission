package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest);

  void delete(UUID userId);
}
