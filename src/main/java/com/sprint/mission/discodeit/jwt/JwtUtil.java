package com.sprint.mission.discodeit.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // 예외 추가
import io.jsonwebtoken.ExpiredJwtException;      // 예외 추가
import io.jsonwebtoken.MalformedJwtException;     // 예외 추가
import jakarta.annotation.PostConstruct;          // PostConstruct 추가
import org.slf4j.Logger;                       // 로거 추가
import org.slf4j.LoggerFactory;                // 로거 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Key 대신 SecretKey 사용 권장
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component("jwtUtil")
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class); // 로거 추가

    @Value("${jwt.secret}") // 설정 파일에서 비밀키 읽어오기
    private String secretKeyString;

    // 만료 시간도 설정 파일에서 읽어오려면 추가
    // @Value("${jwt.expiration}")
    // private long expirationTime;

    private SecretKey key; // Key 타입 변경 및 final 제거

    @PostConstruct
    protected void init() {
        log.info("Loaded jwt.secret from properties: '{}'", secretKeyString);
        if (secretKeyString == null || secretKeyString.trim().isEmpty()) {
            log.error("FATAL: jwt.secret is not configured properly in application properties!");
            throw new IllegalStateException("jwt.secret configuration is missing or empty.");
        }
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8); // <-- Base64 디코딩 없음!
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT Secret Key initialized from string.");
        if (keyBytes.length * 8 < 256) {
            log.warn("The configured JWT secret key's size is less than 256 bits...");
        }
    }
    // 토큰에서 사용자 ID 추출
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 모든 클레임 추출 (예외 처리 추가)
    public Claims extractAllClaims(String token) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 초기화된 key 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
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

    // 토큰 생성 (내부 메서드) - 만료 시간 설정 주의
    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        // !!! 중요: 유효 기간을 적절하게 설정하세요 (예: 1시간, 1일) !!!
        // long validity = expirationTime; // 설정 파일 값 사용 예시
        long validity = 1000L * 60 * 60; // 예시: 1시간 (밀리초)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 사용자 ID 저장
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validity)) // 적절한 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 알고리즘 명시 및 key 사용
                .compact();
    }

    // 토큰 유효성 검증 (만료 + 서명 동시 확인)
    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 파싱 시도 (서명, 만료 자동 확인)
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) { // 그 외 예외 처리
            log.error("Unexpected error during token validation: {}", e.getMessage());
        }
        return false;
    }

    // 토큰 만료 확인 (validateToken으로 대체 가능)
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 만료 예외 발생 시 만료된 것으로 간주
        } catch (Exception e){
            return true; // 다른 예외 발생 시 유효하지 않은 토큰으로 간주 (만료로 처리)
        }
    }

    // 만료일 추출
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}