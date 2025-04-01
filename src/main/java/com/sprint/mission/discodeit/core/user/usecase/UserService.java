package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UpdateUserRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

  void reset(boolean adminAuth);

  UUID create(UserCreateRequestDTO userCreateDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

  UserFindDTO findById(UUID userId);

  boolean existsById(UUID userId);

  List<UserFindDTO> listAllUsers();

  void delete(UUID userId);

  UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO);

}
