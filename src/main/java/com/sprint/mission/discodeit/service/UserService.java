package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.dto.service.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest createRequest,
      BinaryContentCreateRequest binaryRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest updateRequest,
      BinaryContentCreateRequest binaryRequest);

  void delete(UUID userId);

  Optional<User> findByUsername(String username);
  
}