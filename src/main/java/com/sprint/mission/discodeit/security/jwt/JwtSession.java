package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "jwt_sessions")
@Entity
public class JwtSession extends BaseUpdatableEntity {

  @Column(updatable = false, nullable = false)
  private UUID userId;

  @Column(nullable = false, unique = true)
  private String accessToken;

  @Column(nullable = false, unique = true)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expirationTime;

  public JwtSession(UUID userId, String accessToken, String refreshToken, Instant expirationTime) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }

  public void update(String accessToken, String refreshToken, Instant expirationTime) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }
}
