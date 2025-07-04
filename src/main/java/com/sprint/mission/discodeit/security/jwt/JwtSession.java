package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "jwt_sessions") // DB에 이 이름으로 테이블이 생성돼
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  // User 엔티티와 다대일(N:1) 관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 리프레시 토큰은 중복되면 안 되므로 unique = true
  @Column(nullable = false, unique = true, length = 36)
  private String refreshToken;

  public JwtSession(User user, String refreshToken) {
    this.user = user;
    this.refreshToken = refreshToken;
  }
}