package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  UserDto findById(UUID userId);

  UserDto findByUsername(String username);

  UserDto findByEmail(String email);

  List<UserDto> findAll();

  UserDto updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  void deleteUser(UUID userId);

}
