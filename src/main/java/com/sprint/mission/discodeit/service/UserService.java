package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // User create(String username, String email, String password);
    UserResponseDTO create(CreateUserDTO createUserDTO);

    // User find(UUID userId);
    UserResponseDTO find(UUID id);

    // List<User> findAll();
    List<UserResponseDTO> findAll();

    // User update(UUID userId, String newUsername, String newEmail, String newPassword);
    void update(UpdateUserDTO updateUserDTO);

    void delete(UUID userId);
}
