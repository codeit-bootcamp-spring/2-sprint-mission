package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity._User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  _User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  _User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  void delete(UUID userId);
}
