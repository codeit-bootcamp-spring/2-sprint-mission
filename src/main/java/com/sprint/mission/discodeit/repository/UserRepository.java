package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("select distinct u from User u "
      + "left join fetch u.profile ")
  List<User> findAllFetch();

  Optional<User> findByUsername(String username);

  List<User> findAllByIdIn(List<UUID> userIds);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
