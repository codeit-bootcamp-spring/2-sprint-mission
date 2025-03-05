package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findByUserId(UUID userId);
    User findByEmail(String email);
    List<User> findAll();
    void delete(UUID userId);
    boolean exists(UUID userId);
}
