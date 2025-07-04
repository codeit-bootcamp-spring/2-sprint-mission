package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final UserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserMapper userMapper;

  @Value("${security.jwt.secret-key}")
  private String secret;
  private SecretKey secretKey;

  // 토큰 만료 시간 설정
  private static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15; // 액세스 토큰 15분
  private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;    // 리프레시 토큰 7일

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 사용자 정보(UserDto)를 기반으로 새로운 액세스 토큰과 리프레시 토큰을 생성합니다.
   */
  @Transactional
  public TokenPair generateTokens(UserDto userDto) {
    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> UserNotFoundException.withId(userDto.id()));

    Instant now = Instant.now();

    // ⭐️ 액세스 토큰 생성
    String accessToken = Jwts.builder()
        .subject(user.getId().toString())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES)))
        .claim("user", userDto) // 페이로드에 사용자 정보(UserDto)를 직접 포함
        .signWith(secretKey)
        .compact();

    // ⭐️ 리프레시 토큰 생성 (단순 랜덤 문자열)
    String refreshToken = UUID.randomUUID().toString();

    // 기존에 있던 세션은 삭제 (하나의 계정으로 하나의 로그인만 유지)
    jwtSessionRepository.deleteByUser_Id(user.getId());
    // 새로운 리프레시 토큰을 DB에 저장
    JwtSession jwtSession = new JwtSession(user, refreshToken);
    jwtSessionRepository.save(jwtSession);

    return new TokenPair(accessToken, refreshToken);
  }

  /**
   * 토큰의 유효성을 검사하고, 유효하다면 토큰의 내용(Claims)을 반환합니다.
   */
  public Optional<Claims> validateToken(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
      return Optional.of(claims);
    } catch (Exception e) {
      // 토큰이 유효하지 않은 경우 (만료, 변조 등)
      return Optional.empty();
    }
  }

  /**
   * 리프레시 토큰을 사용하여 새로운 액세스/리프레시 토큰 쌍을 발급합니다. (Refresh Token Rotation)
   */
  @Transactional
  public Optional<TokenPair> refreshAccessToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .map(jwtSession -> {
          // DB에서 사용자 정보를 다시 조회
          User user = jwtSession.getUser();
          UserDto userDto = userMapper.toDto(user);

          // 새 토큰 쌍 생성 (이 과정에서 기존 JwtSession은 삭제되고 새로 생성됨)
          return generateTokens(userDto);
        });
  }

  /**
   * 리프레시 토큰을 무효화합니다. (로그아웃 시 사용)
   */
  @Transactional
  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.deleteByRefreshToken(refreshToken);
  }

  /**
   * 특정 사용자의 모든 JwtSession을 삭제합니다. (권한 변경 등에 따른 강제 로그아웃 시 사용)
   */
  @Transactional
  public void invalidateAllTokensForUser(UUID userId) {
    jwtSessionRepository.deleteByUser_Id(userId);
  }

  // 액세스 토큰과 리프레시 토큰을 함께 다루기 위한 record
  public record TokenPair(String accessToken, String refreshToken) {}
}