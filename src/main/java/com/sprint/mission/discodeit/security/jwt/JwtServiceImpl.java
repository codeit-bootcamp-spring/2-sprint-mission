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
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public JwtSession registerJwtSession(UserDto userDto) {
        // 기존 로그인 세션이 있으면 삭제 (동시 로그인 제한)
        User user = userRepository.findById(userDto.id())
            .orElseThrow(() -> UserNotFoundException.withId(userDto.id()));

        jwtSessionRepository.deleteAllByUser(user); // 동시 로그인 제한 처리

        // access, refresh 토큰 생성
        JwtObject accessJwt = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject refreshJwt = generateJwtObject(userDto, refreshTokenValiditySeconds);

        log.debug("저장 전 refreshToken = {}", refreshJwt.token());

        JwtSession session = JwtSession.builder()
            .user(user)
            .accessToken(accessJwt.token())
            .refreshToken(refreshJwt.token())
            .expirationTime(accessJwt.exp())
            .build();

        JwtSession saved = jwtSessionRepository.save(session);
        jwtSessionRepository.flush();
        log.debug("저장 완료 refreshToken = {}", saved.getRefreshToken());

        return saved;
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
    @Transactional
    public JwtSession refreshJwtSession(String refreshToken) {
        log.debug("리프레시 토큰 재발급 요청: {}", refreshToken);

        Optional<JwtSession> optionalSession = jwtSessionRepository.findByRefreshToken(
            refreshToken);
        log.debug("조회 결과 존재 여부 = {}", optionalSession.isPresent());

        JwtSession session = optionalSession.orElseThrow(() -> {
            log.warn("해당 refreshToken으로 JwtSession을 찾을 수 없음");
            return new DiscodeitException(
                ErrorCode.INVALID_TOKEN,
                Map.of("refreshToken", refreshToken));
        });

        if (session.isExpired()) {
            log.warn("refreshToken 만료됨: {}", refreshToken);
            jwtBlacklist.add(session.getAccessToken(), session.getExpirationTime());
            jwtSessionRepository.delete(session);
            throw new DiscodeitException(
                ErrorCode.EXPIRED_TOKEN,
                Map.of("refreshToken", refreshToken));
        }

        UserDto userDto = userMapper.toDto(session.getUser());

        JwtObject newAccessJwt = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject newRefreshJwt = generateJwtObject(userDto, refreshTokenValiditySeconds);

        log.debug("새 accessToken = {}", newAccessJwt.token());
        log.debug("새 refreshToken = {}", newRefreshJwt.token());

        session.update(newAccessJwt.token(), newRefreshJwt.token(), newRefreshJwt.exp());
        return session;
    }

    @Override
    @Transactional
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
    @Transactional
    public void invalidateAllJwtSessionsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserNotFoundException.withId(userId));

        List<JwtSession> sessions = jwtSessionRepository.findAllByUser(user);

        sessions.forEach(session ->
            jwtBlacklist.add(session.getAccessToken(), session.getExpirationTime())
        );
        jwtSessionRepository.deleteAll(sessions);
    }

    @Override
    public List<JwtSession> getActiveJwtSessions() {
        return jwtSessionRepository.findAll().stream()
            .filter(session -> !session.isExpired())
            .toList();
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
