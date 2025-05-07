package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest useRequest, BinaryContentCreateRequest profileRequest);

  UserDto read(UUID userKey);

  List<UserDto> readAll();

  UserDto update(UUID userKey, UserUpdateRequest userRequest,
      BinaryContentCreateRequest profileRequest);

  void delete(UUID userKey);
}
