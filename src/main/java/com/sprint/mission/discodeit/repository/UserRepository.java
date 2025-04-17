package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  @Query("select u from User u join fetch u.profile where u.id = :id")
  User findByIdWithProfile(@Param("id") UUID id);

  @Query("select u from User u join fetch u.profile")
  List<User> findAllWithProfile();

  Optional<User> findByUsername(String username);
}
