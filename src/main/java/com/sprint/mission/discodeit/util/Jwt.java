package com.sprint.mission.discodeit.util;

import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Getter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import java.util.*;
@Builder(toBuilder = true)
@Getter
public class Jwt {
    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 86400000;

    private Jwt() {
    }

    // JWT 생성 메서드
    public static String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId)          // 토큰의 주체: 사용자 ID
                .setIssuedAt(now)            // 발행 시간
                .setExpiration(expiryDate)   // 만료 시간
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증 메서드: 유효하면 사용자 ID(subject)를 반환
    public static String validateToken(String token) {
        Claims claims = Jwts.parser()  // Jwts.parser() 사용
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))  // 서명 키 설정
                .parseClaimsJws(token)  // JWT 파싱
                .getBody();  // Claims 객체 추출

        return claims.getSubject();  // 사용자 ID(subject) 반환
    }
}