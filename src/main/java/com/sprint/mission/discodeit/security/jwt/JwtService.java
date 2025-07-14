package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;
  private final SecureRandom secureRandom = new SecureRandom();
  private final JwtBlacklist jwtBlacklist;

  private static final int MAX_ACTIVE_TOKENS_PER_USER = 3;
  public static final String REFRESH_TOKEN = "refresh_token";

  @Transactional
  public JwtSession generationToken(UserDto userDto) {
    Instant now = Instant.now();
    Instant exp = now.plusMillis(jwtProperties.accessTokenExpiration());
    Instant expiresAt = now.plusMillis(jwtProperties.refreshTokenExpiration());

    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> UserNotFoundException.withId(userDto.id()));

    var accessBuilder = Jwts.builder()
        .subject(userDto.username())
        .issuer(jwtProperties.issuer())
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .claim("userDto", userDto);

    String accessToken = accessBuilder.signWith(secretKey).compact();
    String refreshToken = createRefreshToken(user);

    JwtSession jwtSession = new JwtSession(user, accessToken, refreshToken, expiresAt);
    jwtSessionRepository.save(jwtSession);
    return jwtSession;
  }

  public boolean validateToken(String token) {
    if (jwtBlacklist.contains(token)) {
      log.debug("블랙리스트에 포함된 액세스 토큰");
      return false;
    }

    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .requireIssuer(jwtProperties.issuer())
          .build()
          .parseSignedClaims(token);
    } catch (JwtException e) {
      log.debug("JWT 토큰 유효성 검사 실패: {}", e.getMessage());
      return false;
    }

    boolean exists = jwtSessionRepository.existsByAccessTokenAndRevokedFalse(token);

    if (!exists) {
      log.debug("활성된 세션이 아님");
    }

    return exists;
  }

  @Transactional
  public void invalidateJwtSession(String accessToken) {
    jwtSessionRepository.findByAccessToken(accessToken)
        .ifPresent(this::deleteExpiredToken);
  }

  @Transactional
  public JwtSession findJwtSessionByRefreshToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .filter(JwtSession::isValid)
        .orElseThrow(InvalidCredentialsException::invalidUser);
  }

  @Transactional
  public void deleteExpiredToken(JwtSession jwtSession) {
    if (!jwtSession.isExpired()) {
      jwtBlacklist.add(jwtSession.getAccessToken(), jwtSession.getExpiresAt());
    }
    jwtSessionRepository.delete(jwtSession);
    log.debug("로그아웃/만료되어 JWT 삭제");
  }

  @Transactional
  public JwtSession refreshToken(String refreshToken) {
    log.debug("토큰 리프레시 시작");
    JwtSession jwtSession = findJwtSessionByRefreshToken(refreshToken);
    jwtSession.setRevoked(true);

    jwtBlacklist.add(jwtSession.getAccessToken(), jwtSession.getExpiresAt());
    UserDto userDto = userService.find(jwtSession.getUser().getId());

    return generationToken(userDto);
  }

  @Transactional
  public UserDto getUserDtoFromAccessToken(String accessToken) {
    JwtSession jwtSession = findJwtSessionByAccessToken(accessToken);
    return userService.find(jwtSession.getUser().getId());
  }

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void clearOldTokens() {
    log.debug("만료된 RefreshToken을 가진 JwtSession 자동 삭제 시작");
    Instant expiresAt = Instant.now().plusSeconds(900);
    jwtSessionRepository.deleteAllByRevokedTrueAndExpiresAtBefore(expiresAt);
  }

  private JwtSession findJwtSessionByAccessToken(String accessToken) {
    return jwtSessionRepository.findByAccessToken(accessToken)
        .filter(JwtSession::isValid)
        .orElseThrow(InvalidCredentialsException::invalidUser);
  }

  private String createRefreshToken(User user) {
    long activeTokenCnt = jwtSessionRepository.countActiveTokensByUser(user);

    if (activeTokenCnt >= MAX_ACTIVE_TOKENS_PER_USER) {
      updateOldTokenRevoked(user);
    }

    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  private void updateOldTokenRevoked(User user) {
    jwtSessionRepository.findTopByUserAndRevokedFalseOrderByCreatedAtAsc(user)
        .ifPresent(jwtSession -> {
          jwtSession.setRevoked(true);
          jwtSessionRepository.save(jwtSession);
        });
  }
}
