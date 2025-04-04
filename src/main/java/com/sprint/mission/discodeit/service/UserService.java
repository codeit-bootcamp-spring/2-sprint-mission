package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.SaveUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User save(SaveUserRequestDto saveUserParamDto,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto);

  FindUserDto findByUser(UUID userId);

  List<FindUserDto> findAllUser();

  void update(UUID userId, UpdateUserRequestDto updateUserDto,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto);

  void delete(UUID userId);
}
