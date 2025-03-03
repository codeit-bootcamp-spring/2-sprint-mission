package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(String name);
    Optional<User> getUserById(UUID id);
    List<User> getUsersByName(String name);
    List<User> getAllUsers();
    void updateUserName(UUID id, String name);
    void deleteUser(UUID id);
}
