package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.security.InvalidTokenException;
import com.sprint.mission.discodeit.exception.security.TokenNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
  private static final String USER_DTO_CLAIM = "userDto";

  private final JwtSessionRepository jwtSessionRepository;
  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;
  private final JwtBlacklist jwtBlacklist;

  // UserDto로 토큰 생성
  @Transactional
  public JwtSession generateTokenPair(UserDto userDto) {
    // 기존 세션이 있다면 무효화 -> 동시 로그인 제한
    jwtSessionRepository.findByUserId(userDto.id())
        .ifPresent(this::invalidate);

    Instant now = Instant.now();
    Instant refreshTokenExpiration = now.plusMillis(jwtProperties.refreshTokenExpiration());

    String accessToken = createAccessToken(userDto, now);
    String refreshToken = createRefreshToken();

    // findById 대신 getReferenceById를 사용하여 불필요한 select 쿼리를 줄임
    User user = userRepository.getReferenceById(userDto.id());

    JwtSession session = new JwtSession(user.getId(), accessToken, refreshToken,
        refreshTokenExpiration);
    jwtSessionRepository.save(session);

    return session;
  }

  // 토큰의 유효성 검사
  public boolean validateToken(String token) {
    if (jwtBlacklist.isBlacklisted(token)) {
      log.debug("JWT 블랙리스트에 등록된 토큰입니다. {}", token);
      return false;
    }

    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .requireIssuer(jwtProperties.issuer())
          .build()
          .parseSignedClaims(token); //자동으로 만료 시간도 체크
      return true;
    } catch (ExpiredJwtException e) {
      log.debug("JWT 토큰이 만료되었습니다: {}", e.getMessage());
      return false;
    } catch (UnsupportedJwtException e) {
      log.debug("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
      return false;
    } catch (MalformedJwtException e) {
      log.debug("JWT 토큰 형식이 올바르지 않습니다: {}", e.getMessage());
      return false;
    } catch (SignatureException e) {
      log.debug("JWT 토큰의 서명이 유효하지 않습니다: {}", e.getMessage());
      return false;
    } catch (IllegalArgumentException e) {
      log.debug("JWT 토큰이 비어있거나 올바르지 않습니다: {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      log.debug("JWT 토큰 검증 실패: {}", e.getMessage());
      return false;
    }
  }

  // rotate 전략을 사용하여 액세스 토큰 재발급
  @Transactional
  public JwtSession rotateRefreshToken(String oldRefreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(oldRefreshToken)
        .orElseThrow(() -> TokenNotFoundException.refreshTokenNotFound(oldRefreshToken));

    if (session.isExpired()) {
      jwtSessionRepository.delete(session);
      throw InvalidTokenException.invalidAccessToken(oldRefreshToken);
    }

    UUID userId = session.getUserId();

    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.byId(userId));

    Instant now = Instant.now();
    Instant newRefreshTokenExpiration = now.plusMillis(jwtProperties.refreshTokenExpiration());

    String newAccessToken = createAccessToken(userDto, now);
    String newRefreshToken = createRefreshToken();

    session.updateTokens(newAccessToken, newRefreshToken, newRefreshTokenExpiration);

    return session;
  }

  // 리프레시 토큰 무효화
  public void invalidateJwtSession(UUID userId) {
    jwtSessionRepository.findByUserId(userId)
        .ifPresent(this::invalidate);
  }

  public void invalidateJwtSession(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(this::invalidate);
  }

  // 페이로드 파싱
  public JwtPayload parsePayload(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    UserDto userDto = objectMapper.convertValue(claims.get(USER_DTO_CLAIM), UserDto.class);
    Instant iat = claims.getIssuedAt().toInstant();
    Instant exp = claims.getExpiration().toInstant();

    return new JwtPayload(iat, exp, userDto, token);
  }

  public JwtSession getJwtSession(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> TokenNotFoundException.refreshTokenNotFound(refreshToken));
  }

  public List<JwtSession> getActiveJwtSessions() {
    return jwtSessionRepository.findAllByExpiresAtAfter(Instant.now());
  }

  public boolean isUserOnline(UUID userId) {
    return jwtSessionRepository.existsByUserId(userId);
  }

  private void invalidate(JwtSession session) {
    try {
      JwtPayload payload = parsePayload(session.getAccessToken());
      jwtBlacklist.add(payload.token(), payload.expirationTime());
    } catch (JwtException e) {
      log.warn("블랙리스트 처리를 위해 액세스 토큰을 파싱하는 데 실패했습니다: {}", e.getMessage());
    }

    jwtSessionRepository.delete(session);
  }

  private String createAccessToken(UserDto userDto, Instant now) {
    Instant expiration = now.plusMillis(jwtProperties.accessTokenExpiration());

    // UserDto를 Map으로 변환하여 클레임에 추가
    Map<String, Object> userDtoClaims = objectMapper.convertValue(
        userDto, new TypeReference<>() {
        }
    );

    return Jwts.builder()
        .subject(userDto.username())
        .issuer(jwtProperties.issuer())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .claim(USER_DTO_CLAIM, userDtoClaims)
        .signWith(secretKey)
        .compact();
  }

  private String createRefreshToken() {
    byte[] randomBytes = new byte[32];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }


}
