package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.jwt.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {

  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;
  private final SecureRandom secureRandom = new SecureRandom();
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final JwtProperties jwtProperties;
  private final JwtBlacklist jwtBlacklist;

  @Transactional
  public JwtSession generateJwtSession(UserDto userDto) {
    String refreshToken = generateSecureRandomToken();
    String accessToken = jwtUtil.generateAccessToken(userDto);

    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> new UserNotFoundException(Map.of("id", userDto.id())));

    JwtSession jwtSession = JwtSession.builder()
        .accessToken(accessToken)
        .user(user)
        .refreshToken(refreshToken)
        .refreshTokenExpiresAt(LocalDateTime.now()
            .plusSeconds(jwtProperties.refreshTokenExpiration() / 1000))
        .issuedAt(LocalDateTime.now())
        .build();
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  public boolean validateAccessToken(String token) {
    if (jwtBlacklist.isBlacked(token)) {
      return false;
    }
    return jwtUtil.validateToken(token);
  }

  @Transactional(readOnly = true)
  public JwtSession findByRefreshToken(String token) {
    return jwtSessionRepository.findByRefreshToken(token)
        .orElseThrow(() -> new InvalidRefreshTokenException(
            Map.of("token", token, "details", "유효하지 않은 Refresh Token 입니다.")));
  }

  @Transactional
  public String refreshAccessToken(String token) {
    // Refresh Token 검증 및 조회
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(token)
        .orElseThrow(() -> new InvalidRefreshTokenException(Map.of("refreshToken", token)));

    // Refresh Token이 만료됐는지 확인
    if (jwtSession.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
      jwtSessionRepository.deleteById(jwtSession.getId()); // 만료됐다면 삭제
      throw new InvalidRefreshTokenException(
          Map.of("refreshToken", token, "details", "Refresh token has expired"));
    }

    User user = jwtSession.getUser();

    String newAccessToken = jwtUtil.generateAccessToken(userMapper.toUserDto(user));

    // RefreshToken rotation
    String refreshToken = generateSecureRandomToken();
    LocalDateTime expiresAt = LocalDateTime.now()
        .plusSeconds(jwtProperties.refreshTokenExpiration() / 1000);
    jwtSession.rotateRefreshToken(refreshToken, expiresAt);
    jwtSessionRepository.save(jwtSession);

    return newAccessToken;
  }

  @Transactional
  public void invalidateRefreshToken(String token) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(token)
        .orElseThrow(() -> new InvalidRefreshTokenException(Map.of("refreshToken", token)));
    String accessToken = jwtSession.getAccessToken();
    jwtBlacklist.addBlackList(accessToken, jwtUtil.extractExpiration(accessToken));

    jwtSessionRepository.deleteById(jwtSession.getId());
  }

  @Transactional
  public void invalidateByUserId(UUID id) {
    List<JwtSession> jwtSessionList = jwtSessionRepository.findByUserId(id);
    jwtSessionList.forEach(
        jwtSession -> jwtBlacklist.addBlackList(jwtSession.getAccessToken(),
            jwtUtil.extractExpiration(
                jwtSession.getAccessToken())));
    jwtSessionRepository.deleteByUserId(id);
  }

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void cleanExpiredJwtSession() {
    jwtSessionRepository.deleteByRefreshTokenExpiresAtBefore(LocalDateTime.now());
    log.info("만료된 Refresh Token 삭제 스케쥴링 작업 완료");
  }


  private String generateSecureRandomToken() {
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }
}
