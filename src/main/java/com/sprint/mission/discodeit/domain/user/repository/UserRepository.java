package com.sprint.mission.discodeit.domain.user.repository;

import com.sprint.mission.discodeit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    boolean existsUserByName(String name);

    boolean existsUserByEmail(String email);
}
