package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserCreateResponse save(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto);

  FindUserDto findByUser(UUID userId);

  List<FindUserDto> findAllUser();

  UserUpdateResponse update(UUID userId, UserUpdateRequest updateUserDto,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto);

  void delete(UUID userId);
}
