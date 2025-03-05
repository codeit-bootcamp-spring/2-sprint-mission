package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserRepository {
    void create(User user);
    void update(User user);
    void delete(UUID id);
}