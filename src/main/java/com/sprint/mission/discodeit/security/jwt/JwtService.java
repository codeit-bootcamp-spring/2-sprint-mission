package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final JpaUserRepository userRepository;
  private final JwtBlacklist jwtBlacklist;

  @Value("${jwt.secret}")
  private String secretString;

  @Value("${jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
  }

  @Transactional
  public JwtSession generateTokens(UserDto userDto) {
    Instant now = Instant.now();
    String accessToken = createToken(now, userDto, accessTokenExpiration);
    String refreshToken = createToken(now, null, refreshTokenExpiration);

    JwtSession session = JwtSession.builder()
        .userId(userDto.id())
        .username(userDto.username())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    jwtSessionRepository.findByUserId(userDto.id())
        .ifPresent(existingSession -> session.setId(existingSession.getId()));

    return jwtSessionRepository.save(session);
  }

  public boolean validateToken(String token) {
    if (jwtBlacklist.isBlacklisted(token)) {
      return false;
    }

    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Claims getClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

  @Transactional
  public Optional<JwtSession> refreshAccessToken(String oldRefreshToken) {
    if (!validateToken(oldRefreshToken)) {
      return Optional.empty();
    }
    return jwtSessionRepository.findByRefreshToken(oldRefreshToken).map(session -> {
          String oldAccessToken = session.getAccessToken();
          jwtBlacklist.addBlacklist(oldAccessToken);

          User user = userRepository.findById(session.getUserId()).orElseThrow(
              () -> new UserException(ErrorCode.USER_NOT_FOUND)
          );
          Instant now = Instant.now();
          UserDto userDto = UserDto.from(user);

          String newAccessToken = createToken(now, userDto, accessTokenExpiration);
          String newRefreshToken = createToken(now, null, refreshTokenExpiration);

          session.updateTokens(newAccessToken, newRefreshToken);
          return jwtSessionRepository.save(session);
        }
    );
  }

  @Transactional
  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.deleteByRefreshToken(refreshToken);
  }

  private String createToken(Instant now, UserDto userDto, long expirationInSeconds) {
    Date issuedAt = Date.from(now);
    Date expiration = Date.from(now.plusSeconds(expirationInSeconds));

    ClaimsBuilder claimsBuilder = Jwts.claims()
        .issuer("discodeit")
        .issuedAt(issuedAt)
        .expiration(expiration);

    if (userDto != null) {
      claimsBuilder.add("userDto", userDto);
    }

    return Jwts.builder()
        .claims(claimsBuilder.build())
        .signWith(secretKey)
        .compact();
  }
}
