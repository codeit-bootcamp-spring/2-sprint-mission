package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "jwt_session")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession {

  @Id
  private UUID id;

  @Column(name = "access_token", nullable = false)
  private String accessToken;

  @Column(name = "refresh_token", nullable = false)
  private String refreshToken;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @LastModifiedDate
  private Instant updatedAt;

  @CreatedDate
  private Instant createdAt;

  public JwtSession(String accessToken, String refreshToken, User user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;
  }

  public void update(String newAccessToken, String newRefreshToken) {
    if (newAccessToken != null && !this.accessToken.equals(newAccessToken)) {
      this.accessToken = newAccessToken;
    }
    if (newRefreshToken != null && !this.refreshToken.equals(newRefreshToken)) {
      this.refreshToken = newRefreshToken;
    }
  }

}
