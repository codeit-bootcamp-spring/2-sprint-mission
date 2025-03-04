package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String nickname, String email, String avatar, String status);
    User getUserByUserId(UUID userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(UUID userId, String nickname, String avatar, String status);
    void deleteUserByEmail(String email);
    void deleteUserById(UUID userId);
    void validateUserId(UUID userId);
}
