package com.sprint.mission.discodeit.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.sprint.mission.discodeit.dto.data.UserDto;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.access-token-expiry-seconds}")
  private long accessTokenExpiry;

  @Value("${jwt.refresh-token-expiry-seconds}")
  private long refreshTokenExpiry;

  private SecretKey key;

  private final Map<String, JwtSession> sessionStore = new HashMap<>();

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateAccessToken(UserDto userDto) {
    Instant now = Instant.now();
    return Jwts.builder()
        .claim("userId", userDto.id())
        .claim("email", userDto.email())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(accessTokenExpiry)))
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken(UserDto userDto) {
    Instant now = Instant.now();
    return Jwts.builder()
        .claim("userId", userDto.id())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(refreshTokenExpiry)))
        .signWith(key)
        .compact();
  }

  public JwtSession saveSession(UserDto userDto, String accessToken, String refreshToken) {
    JwtSession session = JwtSession.builder()
        .id(UUID.randomUUID())
        .user(userDto)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(refreshTokenExpiry))
        .build();

    sessionStore.put(refreshToken, session);
    return session;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public UserDto parseToken(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    UUID id = UUID.fromString(claims.get("userId", String.class));
    String email = claims.get("email", String.class);
    return new UserDto(id, null, email, null, null, null);
  }

  public String reissueAccessToken(String refreshToken) {
    JwtSession session = sessionStore.get(refreshToken);
    if (session == null || !validateToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }
    String newAccessToken = generateAccessToken(session.getUser());
    session.setAccessToken(newAccessToken);
    return newAccessToken;
  }

  public void invalidateRefreshToken(String refreshToken) {
    sessionStore.remove(refreshToken);
  }

  public Optional<JwtSession> getSessionByRefreshToken(String refreshToken) {
    return Optional.ofNullable(sessionStore.get(refreshToken));
  }

  public void invalidateUserSessions(UUID userId) {
    sessionStore.entrySet().removeIf(entry -> userId.equals(entry.getValue().getUser().id()));
  }

  public long getRefreshTokenExpiry() {
    return refreshTokenExpiry;
  }

}