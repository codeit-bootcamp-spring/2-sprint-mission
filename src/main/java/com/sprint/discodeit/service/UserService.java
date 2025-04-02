package com.sprint.discodeit.service;

import com.sprint.discodeit.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String username, String email, String password);
    User find(UUID userId);
    List<User> findAll();
    User update(UUID userId, String newUsername, String newEmail, String newPassword);
    void delete(UUID userId);
}
