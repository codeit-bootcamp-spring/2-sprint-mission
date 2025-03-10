package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;


public interface UserService {
    User create(User user);
    User getUser(String name);
    List<User> getAllUser();
    User update(String name, String changeName, String changeEmail);
    void delete(String name);
}
