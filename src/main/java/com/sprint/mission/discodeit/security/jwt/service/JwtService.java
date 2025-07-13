package com.sprint.mission.discodeit.security.jwt.service;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.config.JwtProperties;
import com.sprint.mission.discodeit.security.jwt.repository.JwtSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;

  @Transactional
  public JwtSession generateSession(UserResult userResult) {
    Instant now = Instant.now();
    Instant accessExp = now.plusSeconds(jwtProperties.accessTokenExpiration());
    Instant refreshExp = now.plusSeconds(jwtProperties.refreshTokenExpiration());

    String accessToken = createToken(userResult, now, accessExp);
    String refreshToken = createToken(userResult, now, refreshExp);

    User user = userRepository.findById(userResult.id())
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    JwtSession session = new JwtSession(accessToken, refreshToken, user);
    return jwtSessionRepository.save(session);
  }

  @Transactional
  public JwtSession refreshAccessToken(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("유효하지 않은 리프레시 토큰입니다."));

    if (!validateToken(refreshToken)) {
      throw new RuntimeException("리프레시 토큰이 만료되었습니다.");
    }

    User user = session.getUser();
    UserResult userResult = UserResult.fromEntity(user, true);

    Instant now = Instant.now();
    Instant accessExp = now.plusSeconds(jwtProperties.accessTokenExpiration());
    Instant refreshExp = now.plusSeconds(jwtProperties.refreshTokenExpiration());

    String newAccessToken = createToken(userResult, now, accessExp);
    String newRefreshToken = createToken(userResult, now, refreshExp);
    session.update(newAccessToken, newRefreshToken);

    return jwtSessionRepository.save(session);
  }

  @Transactional
  public void inValidateSession(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("해당 jwt 세션이 없습니다."));

    jwtSessionRepository.delete(jwtSession);
  }

  @Transactional(readOnly = true)
  public String getAccessTokenByRefreshToken(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("해당 jwt 세션이 없습니다."));

    return jwtSession.getAccessToken();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .requireIssuer(jwtProperties.issuer())
          .build()
          .parseSignedClaims(token);
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
