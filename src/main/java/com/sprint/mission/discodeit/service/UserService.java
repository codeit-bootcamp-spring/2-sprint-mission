package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(String name);
    User findById(UUID id);
    List<User> findByName(String name);
    List<User> findAll();
    void updateName(UUID id, String name);
    void delete(UUID id);
}
