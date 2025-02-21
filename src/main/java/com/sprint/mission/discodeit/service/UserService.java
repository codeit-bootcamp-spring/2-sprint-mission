package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User saveUser(String name);
    void findByName(String Name);
    void findAll();
    void update(UUID id, String name);
    void delete(UUID id);
}
