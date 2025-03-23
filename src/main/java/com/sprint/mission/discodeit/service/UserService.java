package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserDTO userDTO);
    User getUser(UUID id);
    List<User> getAllUsers();

    void updateUser(UUID id, String newUsername);
    void deleteUser(UUID id);
}
