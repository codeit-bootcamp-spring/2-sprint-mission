package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${security.jwt.secret}")
  private String secret;
  @Value("${security.jwt.access-token-validity-seconds}")
  private long accessTokenValiditySeconds;
  @Value("${security.jwt.refresh-token-validity-seconds}")
  private long refreshTokenValiditySeconds;

  private SecretKey secretKey;
  private final ObjectMapper objectMapper;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserMapper userMapper;

  @PostConstruct
  public void init() {
    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Transactional
  public TokenPair generateTokens(User user, UserDto userDto) {
    String accessToken = createAccessToken(userDto);
    String refreshToken = createRefreshToken();

    Instant expirationTime = Instant.now().plusSeconds(refreshTokenValiditySeconds);
    JwtSession jwtSession = new JwtSession(user, accessToken, refreshToken, expirationTime);
    jwtSessionRepository.save(jwtSession);

    return new TokenPair(accessToken, refreshToken);
  }

  @Transactional
  public Optional<TokenPair> refreshTokens(String oldRefreshToken) {
    return jwtSessionRepository.findByRefreshToken(oldRefreshToken)
        .filter(session -> !session.isExpired())
        .map(session -> {
          User user = session.getUser();
          UserDto userDto = userMapper.toDto(user);

          String newAccessToken = createAccessToken(userDto);
          String newRefreshToken = createRefreshToken();

          Instant newExpirationTime = Instant.now().plusSeconds(refreshTokenValiditySeconds);
          session.updateTokens(newAccessToken, newRefreshToken, newExpirationTime);

          return new TokenPair(newAccessToken, newRefreshToken);
        });
  }

  @Transactional
  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(jwtSessionRepository::delete);
    log.info("Refresh token invalidated");
  }

  @Transactional
  public void invalidateAllUserSessions(UUID userId) {
    jwtSessionRepository.deleteAllByUserId(userId);
    log.info("All sessions for user {} have been invalidated.", userId);
  }

  public Optional<UserDto> getUserDto(String accessToken) {
    return getClaims(accessToken).map(claims -> {
      try {
        String userDtoJson = claims.get("userDto", String.class);
        return objectMapper.readValue(userDtoJson, UserDto.class);
      } catch (JsonProcessingException e) {
        log.error("Failed to parse userDto from token", e);
        return null;
      }
    });
  }

  private Optional<Claims> getClaims(String token) {
    try {
      return Optional.of(Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload());
    } catch (JwtException e) {
      log.warn("Invalid JWT: {}", e.getMessage());
      return Optional.empty();
    }
  }

  private String createAccessToken(UserDto userDto) {
    try {
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + (accessTokenValiditySeconds * 1000));
      String userDtoJson = objectMapper.writeValueAsString(userDto);

      return Jwts.builder()
          .claim("userDto", userDtoJson)
          .issuedAt(now)
          .expiration(expiryDate)
          .signWith(secretKey)
          .compact();
    } catch (JsonProcessingException e) {
      log.error("Could not create access token", e);
      throw new RuntimeException("Failed to create access token", e);
    }
  }

  private String createRefreshToken() {
    return UUID.randomUUID().toString();
  }

  public record TokenPair(String accessToken, String refreshToken) {}

}
