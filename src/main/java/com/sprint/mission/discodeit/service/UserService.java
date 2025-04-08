package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  UserResponseDto find(UUID userId);

  List<UserResponseDto> findAll();

  User update(UserUpdateRequestDto dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  void delete(UUID userId);
}
