package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String username, String email);
    User findById(UUID userId);
    List<User> findAll();
    User update(UUID userId, String username, String email);
    void deleteById(UUID userId);
}
