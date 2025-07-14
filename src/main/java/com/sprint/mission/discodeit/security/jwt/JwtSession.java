package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseUpdatableEntity {

  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID userId;
  @Column(columnDefinition = "varchar(512)", nullable = false, unique = true)
  private String accessToken;
  @Column(columnDefinition = "varchar(512)", nullable = false, unique = true)
  private String refreshToken;
  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant expiresAt;

  public JwtSession(UUID userId, String accessToken, String refreshToken, Instant expiresAt) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresAt = expiresAt;
  }

  public void updateTokens(String newAccessToken, String newRefreshToken, Instant newExpiresAt) {
    this.accessToken = newAccessToken;
    this.refreshToken = newRefreshToken;
    this.expiresAt = newExpiresAt;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(this.expiresAt);
  }
}
