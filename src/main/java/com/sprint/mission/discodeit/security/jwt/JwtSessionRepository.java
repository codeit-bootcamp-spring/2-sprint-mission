package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  List<JwtSession> findByUser(User user);

  @Modifying
  @Query("DELETE FROM JwtSession js WHERE js.user = :user")
  void deleteByUser(User user);

  @Modifying
  @Query("DELETE FROM JwtSession js WHERE js.refreshTokenExpiresAt < :now")
  void deleteExpiredJwtSession(LocalDateTime now);
}
