package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID userId);
    Optional<User> findByUserName(String userName);
    boolean existsById(UUID userId);
    void deleteById(UUID userId);
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String userEmail);
}