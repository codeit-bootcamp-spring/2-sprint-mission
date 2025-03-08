package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> getUserById(UUID userId);
    List<User> getAllUsers();
    void deleteUser(UUID userId);
}
