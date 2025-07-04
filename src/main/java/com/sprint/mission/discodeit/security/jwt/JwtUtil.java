package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;

  public String generateAccessToken(UserDto userDto) {
    Instant now = Instant.now();
    Instant expiration = now.plusMillis(jwtProperties.accessTokenExpiration());
    String username = userDto.username();

    Map<String, Object> claims = Map.of(
        "id", userDto.id(),
        "email", userDto.email(),
        "role", userDto.role().name()
    );

    var builder = Jwts.builder()
        .subject(username)
        .issuer(jwtProperties.issuer())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration));
    claims.forEach(builder::claim);

    return builder.signWith(secretKey).compact();
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .requireIssuer(jwtProperties.issuer())  // 발급자 검증
          .build()
          .parseSignedClaims(token);
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


  public boolean isTokenExpired(String token) {
    try {
      // 모든 클레임을 추출해서 Expiration 확인
      Claims claims = extractAllClaims(token);
      return claims.getExpiration().before(new Date());
    } catch (JwtException e) {
      log.debug("토큰 만료 확인 중 오류 발생: {}", e.getMessage());
      return true;  // 파싱 실패 시 만료된 것으로 간주
    }
  }


  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }


  public String extractRole(String token) {
    Object role = extractClaim(token, "role");
    return role != null ? role.toString() : null;
  }


  public UUID extractUserId(String token) {
    Object userId = extractClaim(token, "userId");
    if (userId == null) {
      return null;
    }
    try {
      if (userId instanceof UUID) {
        return (UUID) userId;
      } else {
        return UUID.fromString(userId.toString());
      }
    } catch (NumberFormatException e) {
      log.warn("사용자 ID 변환 실패: {}", userId);
      return null;
    }
  }


  public String extractEmail(String token) {
    Object email = extractClaim(token, "email");
    return email != null ? email.toString() : null;
  }


  public Date extractIssuedAt(String token) {
    return extractAllClaims(token).getIssuedAt();
  }


  public Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration();
  }


  public String extractIssuer(String token) {
    return extractAllClaims(token).getIssuer();
  }


  public Object extractClaim(String token, String claimName) {
    return extractAllClaims(token).get(claimName);
  }


  public Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public long getTokenRemainingTime(String token) {
    try {
      Date expiration = extractExpiration(token);
      long remaining = expiration.getTime() - System.currentTimeMillis();
      return Math.max(0, remaining);
    } catch (JwtException e) {
      log.debug("토큰 남은 시간 계산 중 오류 발생: {}", e.getMessage());
      return 0;
    }
  }
}
