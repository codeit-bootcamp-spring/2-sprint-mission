package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User creatUser(User user);
    List<User> findAllUser();
    User findByUserId(UUID userId);
    User updateUser(UUID userId, User user);
    void deleteUser(UUID userId);
}
