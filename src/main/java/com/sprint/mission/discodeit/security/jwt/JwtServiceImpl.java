package com.sprint.mission.discodeit.security.jwt;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  // JWT 비밀키, 만료시간 등은 application.yml 혹은 환경변수에서 주입받아도 됨
  @Value("${jwt.secret}")
  private final String secretKey;// 임시
  private final long accessTokenValiditySeconds = 60 * 15; // 15분
  private final long refreshTokenValiditySeconds = 60 * 60 * 24 * 7; // 7일

  @Override
  @Transactional
  public JwtSession generateTokens(UserDto userDto) {
    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Instant now = Instant.now();
    Date issuedAt = Date.from(now);
    Date accessExp = Date.from(now.plusSeconds(accessTokenValiditySeconds));
    Date refreshExp = Date.from(now.plusSeconds(refreshTokenValiditySeconds));

    // JWT 토큰 생성
    String accessToken = Jwts.builder()
        .setIssuedAt(issuedAt)
        .setExpiration(accessExp)
        .claim("userDto", userDto) // 필요에 따라 직렬화 조심
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();

    String refreshToken = Jwts.builder()
        .setIssuedAt(issuedAt)
        .setExpiration(refreshExp)
        .claim("userDto", userDto)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();

    // DB 저장
    JwtSession session = new JwtSession();
    session.setId(UUID.randomUUID());
    session.setUser(user);
    session.setAccessToken(accessToken);
    session.setRefreshToken(refreshToken);

    jwtSessionRepository.save(session);

    return session;
  }

  @Override
  public boolean isValidToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))         // 서명 키 검증
          .build()
          .parseSignedClaims(token); // 서명된 클레임 파싱

      return true; // 유효한 토큰
    } catch (JwtException | IllegalArgumentException e) {
      // JwtException: 서명 문제, 만료, 포맷 오류 등
      return false; // 유효하지 않은 토큰
    }
  }

  @Override
  @Transactional
  public Optional<JwtSession> reissueAccessToken(String refreshToken) {
    Optional<JwtSession> sessionOpt = jwtSessionRepository.findByRefreshToken(refreshToken);

    if (sessionOpt.isEmpty()) {
      return Optional.empty();
    }

    JwtSession session = sessionOpt.get();

    if (!isValidToken(refreshToken)) {
      // 토큰 만료 또는 위조 등 무효화 처리 (DB 상태 변경 등)
      jwtSessionRepository.delete(session);
      return Optional.empty();
    }

    // 기존 세션 삭제 (Refresh Token Rotation)
    jwtSessionRepository.delete(session);

    UserDto userDto = convertUserToDto(session.getUser());

    // 새로운 토큰 생성 및 저장
    JwtSession newSession = generateTokens(userDto);

    return Optional.of(newSession);
  }

  @Override
  @Transactional
  public void revokeRefreshToken(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(jwtSessionRepository::delete);
  }

  private UserDto convertUserToDto(User user) {
    // User → UserDto 변환 로직 작성
    return userMapper.toDto(user);
  }

  // 토큰에서 클레임 전체 파싱
  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))  // jjwt 0.12.x 버전용
        // 발급자 검증
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // 토큰에서 username (주로 subject) 추출
  @Override
  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public List<GrantedAuthority> extractAuthorities(String token) {
    Claims claims = parseClaims(token);

    // JWT 내부에 roles 클레임이 있다고 가정 (List<String>)
    List<String> roles = claims.get("roles", List.class);

    if (roles == null) {
      return Collections.emptyList();
    }

    return roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
