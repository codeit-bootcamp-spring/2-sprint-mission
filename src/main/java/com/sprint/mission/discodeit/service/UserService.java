package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User saveUser(String name);
    List<User> findByName(String name);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void update(UUID id, String name);
    void delete(UUID id);
}
