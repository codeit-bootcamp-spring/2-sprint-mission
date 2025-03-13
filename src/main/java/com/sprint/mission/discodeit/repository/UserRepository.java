package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    void save(User user);
    User findById(UUID id);
    void delete(UUID id);
    void update(User user);
    List<User> findAll();
    boolean existsById(UUID id);
    boolean existsByUsername(String username);
}
