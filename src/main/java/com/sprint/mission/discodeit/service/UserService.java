package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User readUser(UUID id);
    List<User> readAllUsers();
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
}
