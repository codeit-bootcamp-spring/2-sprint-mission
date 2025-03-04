package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User creatUser(String userName, String userEmail, String userPassword);
    List<User> findAllUser();
    User findByUserId(UUID userId);
    User updateUser(UUID userId, String newUserName, String newUserEmail, String newUserPassword);
    void deleteUser(UUID userId);
}
