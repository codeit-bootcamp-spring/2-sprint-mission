package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;



public interface UserService {
    void createUser(User user);
    void getUser(String name);
    void getAllUsers();
    void updateUser(String name, String changeName, String changeEmail);
    void deleteUser(String name);
}
