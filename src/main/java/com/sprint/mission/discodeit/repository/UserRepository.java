package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    void create(User user);
    void update(User user);
    void delete(UUID id);

    List<User> findAll();
    User find(UUID id);
}