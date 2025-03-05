package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(User user);
    User findById(UUID userId);
    List<User> findAll();
    void delete(UUID userId);
    void update(UUID userId, String nickname, String email, String password);
}
