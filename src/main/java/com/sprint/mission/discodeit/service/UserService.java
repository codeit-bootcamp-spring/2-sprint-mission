package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    void createUser(User user);
    User getUser(String name);
    List<User> getAllUsers();
    void updateUser(String name, String changeName, String changeEmail);
    void deleteUser(String name);
}
