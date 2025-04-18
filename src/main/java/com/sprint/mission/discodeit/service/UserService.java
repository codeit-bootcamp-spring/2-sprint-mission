package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  UserResponse find(UUID userId);

  List<UserResponse> findAll();

  User update(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  void delete(UUID userId);
}
