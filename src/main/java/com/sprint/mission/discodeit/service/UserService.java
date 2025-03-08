package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String userName, String userEmail, String userPassword);
    List<User> findAll();
    User findById(UUID userId);
    User update(UUID userId, String newUserName, String newUserEmail, String newUserPassword);
    void delete(UUID userId);
}
