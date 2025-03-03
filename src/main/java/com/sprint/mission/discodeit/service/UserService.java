package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User getUser(String userName);
    List<User> getAllUsers();
    void registerUser(String userName, String nickName);
    void updateName(String oldUserName, String userName, String newName);
    void deleteUser(String userName);
}
