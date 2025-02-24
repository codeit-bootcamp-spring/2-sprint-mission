package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User find(String identifier);
    List<User> findAll();
    User update(String identifier, User user);
    void delete(String identifier);
}
