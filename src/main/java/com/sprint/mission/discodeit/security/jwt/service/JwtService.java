package com.sprint.mission.discodeit.security.jwt.service;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.config.JwtProperties;
import com.sprint.mission.discodeit.security.jwt.repository.JwtSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final Map<String, Instant> JwtBlacklist = new ConcurrentHashMap<>();

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;

  @Transactional
  public JwtSession generateSession(UserResult userResult) {
    jwtSessionRepository.deleteById(userResult.id());

    Instant now = Instant.now();
    Instant accessExp = now.plusSeconds(jwtProperties.accessTokenExpiration());
    Instant refreshExp = now.plusSeconds(jwtProperties.refreshTokenExpiration());

    String accessToken = createToken(userResult, now, accessExp);
    String refreshToken = createToken(userResult, now, refreshExp);

    User user = userRepository.findById(userResult.id())
        .orElseThrow(() -> new UserNotFoundException(Map.of()));
    JwtSession session = new JwtSession(accessToken, refreshToken, user);

    return jwtSessionRepository.save(session);
  }

  @Transactional
  public JwtSession refreshSession(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("유효하지 않은 리프레시 토큰입니다."));

    if (!validateAccessToken(refreshToken)) {
      throw new RuntimeException("리프레시 토큰이 만료되었습니다.");
    }

    User user = jwtSession.getUser();
    UserResult userResult = UserResult.fromEntity(user, true);

    Instant now = Instant.now();
    Instant accessExp = now.plusSeconds(jwtProperties.accessTokenExpiration());
    Instant refreshExp = now.plusSeconds(jwtProperties.refreshTokenExpiration());

    String newAccessToken = createToken(userResult, now, accessExp);
    String newRefreshToken = createToken(userResult, now, refreshExp);
    jwtSession.update(newAccessToken, newRefreshToken);

    return jwtSessionRepository.save(jwtSession);
  }

  @Transactional
  public void inValidateSession(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("해당 jwt 세션이 없습니다."));

    jwtSessionRepository.delete(jwtSession);
    JwtBlacklist.put(jwtSession.getAccessToken(), Instant.now());
  }

  @Transactional(readOnly = true)
  public String getAccessTokenByRefreshToken(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("해당 jwt 세션이 없습니다."));

    return jwtSession.getAccessToken();
  }

  public boolean validateAccessToken(String accessToken) {
    if (JwtBlacklist.containsKey(accessToken)) {
      return false;
    }

    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .requireIssuer(jwtProperties.issuer())
          .build()
          .parseSignedClaims(accessToken);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public UserResult parseUser(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .requireIssuer(jwtProperties.issuer())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("user", UserResult.class);
  }

  @Scheduled(fixedRate = 1800000)
  void cleanExpiredBlacklistTokens() {
    Instant now = Instant.now();
    JwtBlacklist.entrySet()
        .removeIf(entry -> entry.getValue().isBefore(now));
  }

  private String createToken(UserResult user, Instant now, Instant expiresAt) {
    return Jwts.builder()
        .subject(user.username())
        .issuedAt(Date.from(now))
        .issuer(jwtProperties.issuer())
        .expiration(Date.from(expiresAt))
        .claim("user", user)
        .claim("iat", now.getEpochSecond())
        .claim("exp", expiresAt.getEpochSecond())
        .signWith(secretKey)
        .compact();
  }

}
