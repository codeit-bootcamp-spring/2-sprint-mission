package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class JwtService {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

  @Value("${security.jwt.secret}")
  private String secret;
  @Value("${security.jwt.access-token-validity-seconds}")
  private long accessTokenValiditySeconds;
  @Value("${security.jwt.refresh-token-validity-seconds}")
  private long refreshTokenValiditySeconds;

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;

  public JwtSession registerJwtSession(UserDto userDto) {
    JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
    JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

    JwtSession jwtSession = new JwtSession(
        userDto.id(),
        accessJwtObject.token(),
        refreshJwtObject.token(),
        accessJwtObject.expirationTime()
    );
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  public boolean validate(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);

      return true;
    } catch (JwtException e) {
      log.warn("JWT 예외: {}", e.getMessage());
    }

    return false;
  }

  public JwtSession refreshJwtSession(String refreshToken) {
    if (!validate(refreshToken)) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of("refreshToken", refreshToken));
    }
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND,
            Map.of("refreshToken", refreshToken)));

    UUID userId = parse(refreshToken).userDto().id();

    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
    JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

    session.update(
        accessJwtObject.token(),
        refreshJwtObject.token(),
        accessJwtObject.expirationTime()
    );

    return session;
  }

  private JwtObject generateJwtObject(UserDto userDto, long tokenValiditySeconds) {
    Instant issueTime = Instant.now();
    Instant expirationTime = issueTime.plus(Duration.ofSeconds(tokenValiditySeconds));

    String token = Jwts.builder()
        .subject(userDto.username())
        .claim("userDto", userDto)
        .issuedAt(new Date(issueTime.toEpochMilli()))
        .expiration(new Date(expirationTime.toEpochMilli()))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
        .compact();

    return new JwtObject(
        issueTime,
        expirationTime,
        userDto,
        token
    );
  }

  public JwtObject parse(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> jws = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);

    Claims claims = jws.getPayload();

    Instant issuedAt = claims.getIssuedAt().toInstant();
    Instant expiration = claims.getExpiration().toInstant();
    UserDto userDto = objectMapper.convertValue(claims.get("userDto"), UserDto.class);

    return new JwtObject(issuedAt, expiration, userDto, token);
  }

  public JwtSession getJwtSession(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND,
            Map.of("refreshToken", refreshToken)));
  }

  @Transactional
  public void invalidateJwtSession(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(jwtSessionRepository::delete);
  }

  @Transactional
  public void invalidateJwtSession(UUID userId) {
    jwtSessionRepository.findByUserId(userId)
        .ifPresent(jwtSessionRepository::delete);
  }

}
