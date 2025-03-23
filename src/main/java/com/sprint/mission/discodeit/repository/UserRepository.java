package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void delete(UUID id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

