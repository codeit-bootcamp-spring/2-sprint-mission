package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID userId);
    List<User> findAll();
    void delete(UUID userId);
    boolean exists(UUID userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
}
