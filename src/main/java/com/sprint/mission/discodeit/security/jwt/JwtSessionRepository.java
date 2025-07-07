package com.sprint.mission.discodeit.security.jwt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  List<JwtSession> findByUserId(UUID id);

  @Modifying
  @Query("DELETE FROM JwtSession js WHERE js.user.id = :userId")
  void deleteByUserId(@Param("userId") UUID id);
}
