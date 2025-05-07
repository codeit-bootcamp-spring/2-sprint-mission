package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.profile
        LEFT JOIN FETCH u.status
        WHERE u.username = :username
      """)
  Optional<User> findByUsername(@Param("username") String username);

  @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.profile
        LEFT JOIN FETCH u.status
        WHERE u.email = :email
      """)
  Optional<User> findByEmail(@Param("email") String email);

  @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.profile
        LEFT JOIN FETCH u.status
      """)
  List<User> findAllWithProfileAndStatus();
}
