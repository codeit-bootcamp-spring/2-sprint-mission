package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  UserResponseDto findById(UUID userId);

  UserResponseDto findByUsername(String username);

  UserResponseDto findByEmail(String email);

  List<UserResponseDto> findAll();

  User updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  void deleteUser(UUID userId);

}
