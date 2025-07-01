package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomSessionRepository extends JpaRepository<User, UUID> {

  @Query(value = "SELECT COUNT(*) > 0 FROM SPRING_SESSION WHERE PRINCIPAL_NAME = :username", nativeQuery = true)
  boolean existsByPrincipalName(@Param("username") String principalName);

}
