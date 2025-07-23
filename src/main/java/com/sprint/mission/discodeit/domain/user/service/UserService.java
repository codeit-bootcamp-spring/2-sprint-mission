package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  void delete(UUID userId);
}
