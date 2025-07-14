package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "jwt_sessions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String accessToken;

  @Column(unique = true, nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expirationTime;

  public JwtSession(User user, String accessToken, String refreshToken, Instant expirationTime) {
    this.user = user;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }

  public boolean isExpired() {
    return this.expirationTime.isBefore(Instant.now());
  }

  public void updateTokens(String newAccessToken, String newRefreshToken, Instant newExpirationTime) {
    this.accessToken = newAccessToken;
    this.refreshToken = newRefreshToken;
    this.expirationTime = newExpirationTime;
  }

}
