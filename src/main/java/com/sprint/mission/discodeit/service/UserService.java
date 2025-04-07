package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserCreateResponse create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest);

  UserResponse find(UUID userId);

  List<UserResponse> findAll();

  UserUpdateResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest);

  void delete(UUID userId);
}
