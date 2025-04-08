package com.sprint.mission.discodeit.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

  // 실제 서비스에서는 외부 설정으로 관리해야 하는 비밀키
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  // 토큰에서 사용자 ID 추출
  public String extractUserId(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // 모든 클레임 추출
  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  // 특정 클레임 추출
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // 토큰 생성
  public String generateToken(String userId) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userId);
  }

  // 토큰 생성 (내부 메서드)
  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        // 무한 유효기간 대신 매우 긴 유효기간 설정 (예: 100년)
        .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 100))
        .signWith(key)
        .compact();
  }

  // 토큰 유효성 검증
  public Boolean validateToken(String token) {
    try {
      return !isTokenExpired(token);
    } catch (Exception e) {
      return false;
    }
  }

  // 토큰 만료 확인
  private Boolean isTokenExpired(String token) {
    final Date expiration = extractExpiration(token);
    return expiration.before(new Date());
  }

  // 만료일 추출
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
