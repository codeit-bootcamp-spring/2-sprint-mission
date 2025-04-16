package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJPARepository extends JpaRepository<User, UUID> {

    Boolean existsByUsernameOrEmail(String username, String email);
    List<User> findByIdIn(List<UUID> userIds);
    Optional<User> findByUsername(String username);

    // Query
    @Query("select u from User u left join fetch u.profile where u.id = :id")
    Optional<User> findByIdWithProfile(@Param("id") UUID userId);

    @Query("select u from User u left join fetch u.profile")
    List<User> findAllUsers();

}
