package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.auth.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentCreateRequest);

  UserDto update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentCreateRequest);

  UserDto updateRole(UserRoleUpdateRequest request);

  void delete(UUID id);
}
