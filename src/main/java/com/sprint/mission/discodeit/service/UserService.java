package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.create.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.create.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateUserRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    void reset(boolean adminAuth);

    UUID create(CreateUserRequestDTO userCreateDTO, Optional<CreateBinaryContentRequestDTO> binaryContentDTO);

    UserFindDTO findById(UUID userId);

    boolean existsById(UUID userId);

    List<UserFindDTO> listAllUsers();

    void delete(UUID userId);

    UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO, Optional<CreateBinaryContentRequestDTO> binaryContentDTO);

}
