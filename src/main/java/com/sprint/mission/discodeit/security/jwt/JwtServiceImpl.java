package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secret;
    @Value("${security.jwt.access-token-validity-seconds}")
    private long accessTokenValiditySeconds;
    @Value("${security.jwt.refresh-token-validity-seconds}")
    private long refreshTokenValiditySeconds;

    private final ObjectMapper objectMapper;
    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtBlacklist jwtBlacklist;

    @Override
    public JwtSession registerJwtSession(UserDto userDto) {
        // access, refresh 토큰 생성
        JwtObject accessJwt = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject refreshJwt = generateJwtObject(userDto, refreshTokenValiditySeconds);

        User user = userRepository.findById(userDto.id())
            .orElseThrow(() -> UserNotFoundException.withId(userDto.id()));

        JwtSession session = JwtSession.builder()
            .user(user)
            .accessToken(accessJwt.token())
            .refreshToken(refreshJwt.token())
            .expirationTime(accessJwt.exp())
            .build();

        return jwtSessionRepository.save(session);
    }

    @Override
    public boolean validate(String token) {

        if (jwtBlacklist.contains(token)) {
            log.warn("블랙리스트에 등록된 토큰입니다.");
            return false;
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
            Jwts.parser()
                .verifyWith(key) // 검증에 사용할 시크릿키 지정
                .build()
                .parseSignedClaims(token); // 검증 수행
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public JwtObject parse(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();  // payload 추출

        Instant iat = claims.getIssuedAt().toInstant();
        Instant exp = claims.getExpiration().toInstant();

        UserDto userDto = objectMapper.convertValue(claims.get("userDto"), UserDto.class);

        return new JwtObject(iat, exp, userDto, token);
    }

    // 리프레쉬 토큰 활용 -> 새로운 액세스 토큰 및 리프래쉬 토큰 발급
    @Override
    public JwtSession refreshJwtSession(String refreshToken) {
        JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new DiscodeitException(
                ErrorCode.INVALID_TOKEN,
                Map.of("refreshToken", refreshToken)
            ));

        if (session.isExpired()) {
            jwtBlacklist.add(session.getAccessToken(), session.getExpirationTime());
            jwtSessionRepository.delete(session);
            throw new DiscodeitException(
                ErrorCode.EXPIRED_TOKEN,
                Map.of("refreshToken", refreshToken));
        }

        UserDto userDto = userMapper.toDto(session.getUser());

        JwtObject newAccessJwt = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject newRefreshJwt = generateJwtObject(userDto, refreshTokenValiditySeconds);

        session.update(newAccessJwt.token(), newRefreshJwt.token(), newRefreshJwt.exp());
        return session;
    }

    @Override
    public void invalidateJwtSession(String refreshToken) {
        jwtSessionRepository.findByRefreshToken(refreshToken)
            .ifPresent(session -> {
                jwtBlacklist.add(session.getAccessToken(), session.getExpirationTime());
                jwtSessionRepository.delete(session);
            });
    }

    @Override
    public JwtSession getJwtSession(String refreshToken) {
        return jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND,
                Map.of("refreshToken", refreshToken)));
    }

    @Override
    public void invalidateAllJwtSessionsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserNotFoundException.withId(userId));

        List<JwtSession> sessions = jwtSessionRepository.findAllByUser(user);

        sessions.forEach(session ->
            jwtBlacklist.add(session.getAccessToken(), session.getExpirationTime())
        );
        jwtSessionRepository.deleteAll(sessions);
    }

    // JWT 생성 메서드
    private JwtObject generateJwtObject(UserDto userDto, long tokenValiditySeconds) {
        Instant issuedAt = Instant.now();
        Instant expirationTime = issuedAt.plusSeconds(tokenValiditySeconds);

        String token = Jwts.builder()
            .subject(userDto.username())
            .claim("userDto", userDto)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expirationTime))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();

        return new JwtObject(issuedAt, expirationTime, userDto, token);
    }
}
