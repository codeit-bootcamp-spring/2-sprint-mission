package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    void createUser(User newUser);
    User readUser(UUID id);
    Set<User> readAllUsers();
    void updateUserName(UUID id, String newUserName);
    void updatePassword(UUID id, String newPassword);
    void deleteUser(UUID id);
}