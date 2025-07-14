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

  // 현재 로그인 상태 확인 (jwtSession이 존재하는지 + refreshToken이 만료되진 않았는지)
  @Query("""
      SELECT CASE WHEN COUNT(js) > 0 THEN TRUE ELSE FALSE END
      FROM JwtSession js
      WHERE js.user.id = :userId
      AND js.refreshTokenExpiresAt > CURRENT_TIMESTAMP
      """)
  boolean existsByUserId(@Param("userId") UUID userId);

  @Modifying
  @Query("DELETE FROM JwtSession js WHERE js.user.id = :userId")
  void deleteByUserId(@Param("userId") UUID userId);

  void deleteByRefreshTokenExpiresAtBefore(LocalDateTime localDateTime);
}
