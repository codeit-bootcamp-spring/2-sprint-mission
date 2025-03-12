package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UUID userId);
    List<User> findAll();
    void delete(UUID userId);
    void update(UUID userId, String nickname, String email, String password);
}
