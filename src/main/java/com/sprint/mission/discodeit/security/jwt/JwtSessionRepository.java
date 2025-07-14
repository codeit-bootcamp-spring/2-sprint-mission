package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  Optional<JwtSession> findByAccessToken(String accessToken);

  @Query("SELECT COUNT(js) FROM JwtSession js WHERE js.user = :user AND js.revoked = false")
  long countActiveTokensByUser(User user);

  Optional<JwtSession> findTopByUserAndRevokedFalseOrderByCreatedAtAsc(User user);

  boolean existsByAccessTokenAndRevokedFalse(String accessToken);

  @Query("SELECT js FROM JwtSession js WHERE js.revoked = false AND js.user = :user")
  List<JwtSession> findAllByUserAndRevokedFalse(User user);

  void deleteAllByRevokedTrueAndExpiresAtBefore(Instant expiresAt);
}
