package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JwtBlacklist jwtBlacklist;

    @Value("${jwt.secret-key}")
    private String secretKeyString;
    private SecretKey secretKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30 minutes
    public static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    @Transactional
    public JwtToken createTokens(UserDto userDto) {
        invalidateAllUserSessions(userDto.id());

        String accessToken = generateToken(userDto, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = generateToken(userDto, REFRESH_TOKEN_EXPIRATION);

        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new UserNotFoundException(userDto.id()));

        JwtSession jwtSession = JwtSession.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        jwtSessionRepository.save(jwtSession);
        return new JwtToken(accessToken, refreshToken);
    }

    private String generateToken(UserDto userDto, long expiration) {
        Instant now = Instant.now();
        try {
            return Jwts.builder()
                    .claim("userDto", objectMapper.writeValueAsString(userDto))
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusMillis(expiration)))
                    .signWith(secretKey)
                    .compact();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("토큰 생성 직렬화 실패", e);
        }
    }

    @SuppressWarnings("deprecation")
    public Optional<Claims> validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }

        if (jwtBlacklist.isBlacklisted(token)) {
            return Optional.empty();
        }

        try {
            JwtParser parser = Jwts.parser().setSigningKey(secretKey).build();
            Claims claims = parser.parseClaimsJws(token).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getAccessTokenByRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || validateToken(refreshToken).isEmpty()) {
            return Optional.empty();
        }

        return jwtSessionRepository.findByRefreshToken(refreshToken)
                .map(JwtSession::getAccessToken)
                .filter(accessToken -> !jwtBlacklist.isBlacklisted(accessToken));
    }

    @Transactional
    public Optional<JwtToken> refreshAccessToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            return Optional.empty();
        }

        return jwtSessionRepository.findByRefreshToken(refreshToken)
                .flatMap(session -> validateToken(refreshToken)
                        .map(claims -> {
                            try {
                                String userDtoStr = claims.get("userDto", String.class);
                                UserDto userDto = objectMapper.readValue(userDtoStr, UserDto.class);

                                String newAccessToken = generateToken(userDto, ACCESS_TOKEN_EXPIRATION);
                                String newRefreshToken = generateToken(userDto, REFRESH_TOKEN_EXPIRATION);

                                // 기존 엑세스 토큰을 블랙리스트에 추가
                                addTokenToBlacklist(session.getAccessToken());

                                session.updateTokens(newAccessToken, newRefreshToken);
                                jwtSessionRepository.save(session);

                                return new JwtToken(newAccessToken, newRefreshToken);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("사용자 정보 처리 실패", e);
                            }
                        })
                );
    }

    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            return;
        }

        jwtSessionRepository.findByRefreshToken(refreshToken).ifPresent(session -> {
            addTokenToBlacklist(session.getAccessToken());
            jwtSessionRepository.delete(session);
        });
    }

    @Transactional
    public void invalidateAllUserSessions(UUID userId) {
        List<JwtSession> userSessions = jwtSessionRepository.findAllByUserId(userId);

        userSessions.forEach(session -> addTokenToBlacklist(session.getAccessToken()));

        jwtSessionRepository.deleteByUserId(userId);
    }


    public boolean isUserLoggedIn(UUID userId) {
        return jwtSessionRepository.existsByUserId(userId);
    }


    private void addTokenToBlacklist(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            validateToken(accessToken).ifPresent(claims -> {
                Instant expiryTime = claims.getExpiration().toInstant();
                jwtBlacklist.blacklist(accessToken, expiryTime);
            });
        }
    }
}