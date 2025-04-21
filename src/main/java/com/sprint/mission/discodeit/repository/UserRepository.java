package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    @NonNull
    User save(@NonNull User user);

    @NonNull
    Optional<User> findById(@NonNull UUID id);

    Optional<User> findByUsername(String username);

    @NonNull
    List<User> findAll();

    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
