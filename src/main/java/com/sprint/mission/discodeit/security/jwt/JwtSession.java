package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = 500)
  private String accessToken;

  @Column(nullable = false, length = 500)
  private String refreshToken;

  @Column(nullable = false)
  private LocalDateTime issuedAt;

  @Column
  private LocalDateTime refreshTokenExpiresAt;

  public void updateAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void rotateRefreshToken(String token, LocalDateTime refreshTokenExpiresAt) {
    this.refreshToken = token;
    this.issuedAt = LocalDateTime.now();
    this.refreshTokenExpiresAt = refreshTokenExpiresAt;
  }
}
