package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "jwt_sessions")
@Entity
public class JwtSession extends BaseUpdatableEntity {

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private String accessToken;

  @Column(nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expirationTime;

  public boolean isExpired() {
    return expirationTime.isBefore(Instant.now());
  }

  public void update(String accessToken, String refreshToken, Instant expirationTime) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }
}
