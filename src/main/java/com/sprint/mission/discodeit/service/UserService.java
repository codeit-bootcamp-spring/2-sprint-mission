package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username);
    User getUser(UUID id);
    List<User> getAllUsers();

    void updateUser(UUID id, String newUsername);
    void deleteUser(UUID id);
}
