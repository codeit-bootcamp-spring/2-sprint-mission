package com.sprint.mission.discodeit.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  // 리프레시 토큰으로 세션 정보 찾기
  Optional<JwtSession> findByRefreshToken(String refreshToken);

  // 리프레시 토큰으로 세션 정보 삭제 (로그아웃 시)
  void deleteByRefreshToken(String refreshToken);

  // 사용자 ID로 모든 세션 정보 삭제 (강제 로그아웃 시)
  void deleteByUser_Id(UUID userId);
}