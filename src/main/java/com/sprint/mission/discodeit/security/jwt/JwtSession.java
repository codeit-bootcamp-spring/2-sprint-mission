package com.sprint.mission.discodeit.security.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Setter
  private UUID id;

  @Column(nullable = false, unique = true)
  private UUID userId;

  @Column(nullable = false)
  private String username;

  @Column(length = 512, nullable = false)
  private String accessToken;

  @Column(length = 512, nullable = false)
  private String refreshToken;

  @Builder
  public JwtSession(UUID userId, String username, String accessToken, String refreshToken) {
    this.userId = userId;
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public void updateTokens(String newAccessToken, String newRefreshToken) {
    this.accessToken = newAccessToken;
    this.refreshToken = newRefreshToken;
  }
}
