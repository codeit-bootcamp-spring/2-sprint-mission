package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserResponse create(UserCreateRequest createRequest,
      BinaryContentCreateRequest binaryRequest);

  UserResponse find(UUID userId);

  List<UserResponse> findAll();

  UserResponse update(UUID userId, UserUpdateRequest updateRequest,
      BinaryContentCreateRequest binaryRequest);

  void delete(UUID userId);

  Optional<User> findByUsername(String username);

  UserResponse assembleUserResponse(User user);
}