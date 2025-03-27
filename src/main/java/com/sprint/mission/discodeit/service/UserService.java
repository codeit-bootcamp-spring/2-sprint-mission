package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.CreateUserDTO;
import com.sprint.mission.discodeit.dto.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserDTO dto, Optional<BinaryContentDTO> profileCreateRequest);

    UserResponseDTO searchUser(UUID userId);

    List<UserResponseDTO> searchAllUsers();

    User updateUser(UUID userId, UpdateUserDTO dto,Optional<BinaryContentDTO> profileCreateRequest);

    void deleteUser(UUID userId);

    UserResponseDTO updateOnlineState(UUID userId);
}
