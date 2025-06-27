package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentCreateRequest);

  List<UserDto> findAll();

  UserStatusDto online(UUID userId, UserStatusRequest request);

  UserDto update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentDTO);

  void delete(UUID id);
}
