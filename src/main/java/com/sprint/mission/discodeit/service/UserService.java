package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(String name, String email);
    User getUser(String name);
    List<User> getAllUsers();
    void updateUser(String name, String changeName, String changeEmail);
    void deleteUser(String name);
}
