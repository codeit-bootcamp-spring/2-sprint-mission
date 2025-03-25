package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID userId);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsById(UUID userId);

    void delete(UUID userId);
}
