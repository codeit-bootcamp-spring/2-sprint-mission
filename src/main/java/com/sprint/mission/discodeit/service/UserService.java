package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User createUser(String nickname, String email, String avatar, String status);
    User getUserById(Long userId);
    User getUserByEmail(String email);
    List<User> getUsers();
    User updateUser(Long userId, String nickname, String email, String avatar, String status);
    boolean deleteUserByEmail(User user);
    boolean deleteUserById(Long userId);
}
