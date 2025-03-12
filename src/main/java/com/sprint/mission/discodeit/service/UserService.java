package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String username, String password);
    User findById(UUID userId);
    List<User> findAll();
    User updateName(UUID userId, String newUsername);
    User updatePassword(UUID userId, String newPassword);
    void delete(UUID userId);
}
