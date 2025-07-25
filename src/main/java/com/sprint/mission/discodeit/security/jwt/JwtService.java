package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.access-token-expiry}")
    private long accessTokenExpiryMillis;

    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiryMillis;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // 토큰 생성 후 세션에 저장
    @Transactional
    public JwtSession createSession(UserDto userDto) {
        Instant now = Instant.now();

        String accessToken = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(accessTokenExpiryMillis)))
                .claim("userId", userDto.getId())
                .claim("userDto", userDto)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshTokenExpiryMillis)))
                .claim("userId", userDto.getId())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();

        JwtSession jwtSession = JwtSession.builder()
                .userId(userDto.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .issuedAt(LocalDateTime.ofInstant(now, ZoneId.systemDefault()))
                .expiresAt(LocalDateTime.ofInstant(now.plusMillis(refreshTokenExpiryMillis), ZoneId.systemDefault()))
                .build();

        jwtSessionRepository.save(jwtSession);
        return jwtSession;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSecretKey()) // 서명 검증에 사용할 비밀키 설정. JWT 위조, 변조 여부 검증
                    .build() // parser 객체 생성
                    .parseClaimsJws(token); // 전달받은 JWT 파싱. 서명, 만료, 구조 등 모든 검증 수행
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // userId 값 까지 추가 검증
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // userId 비교
            String tokenUserId = claims.get("userId", String.class);
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            boolean idMatches = tokenUserId.equals(customUserDetails.getUserId().toString());

            boolean notExpired = claims.getExpiration().after(new Date());
            return idMatches && notExpired;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 리프레시 토큰으로 엑세스 토큰 재발급 + 리프레시 토큰 Rotation
    @Transactional
    public Optional<JwtSession> rotateRefreshToken(String refreshToken) {
        // JwtSession 테이블에서 해당 리프레시 토큰이 있는지 조회
        Optional<JwtSession> sessionOpt = jwtSessionRepository.findByRefreshToken(refreshToken);

        // 토큰이 없으면 끝. 잘못된 요청이나 만료된 경우
        if (sessionOpt.isEmpty()) return Optional.empty();

        JwtSession session = sessionOpt.get();

        // 토큰이 유효한지 체크
        if (!validateToken(refreshToken)) {
            jwtSessionRepository.delete(session);
            return Optional.empty();
        }

        // 토큰에 문제 없는 경우 userDto를 통해 다시 세션 생성(access, refresh 토큰 포함)
        UserDto userDto = userRepository.findById(session.getUserId())
                .map(userMapper::toDto)
                .orElse(null);

        if (userDto == null) {
            jwtSessionRepository.delete(session);
            return Optional.empty();
        }

        // 새 토큰 생성
        JwtSession newSession = createSession(userDto);

        // 기존 세션 삭제 (rotation)
        jwtSessionRepository.delete(session);

        return Optional.of(newSession);
    }

    // 리프레시 토큰 무효화(세션 삭제)
    @Transactional
    public void invalidateSession(String refreshToken) {
        jwtSessionRepository.findByRefreshToken(refreshToken)
                .ifPresent(jwtSessionRepository::delete);
    }

    // accessToken에서 사용자 정보 추출
    @Transactional
    public UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.get("userId", String.class));
    }
}
