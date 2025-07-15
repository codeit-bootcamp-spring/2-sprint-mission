package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtBlackList blackList;


    @Value("${discodeit.jwt.secret}")
    private String secret;

    private static final Duration ACCESS_TOKEN_VALIDITY = Duration.ofMinutes(15);
    private static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofMinutes(45);

    @Transactional
    public JwtSession createJwtSession(UserDto userDto) {

        try {


            JwtObject accessJwtObject = createToken(userDto, ACCESS_TOKEN_VALIDITY);
            JwtObject refreshJwtObject = createToken(userDto, REFRESH_TOKEN_VALIDITY);

            JwtSession jwtSession = new JwtSession(
                userDto.id(),
                accessJwtObject.token(),
                refreshJwtObject.token(),
                accessJwtObject.expirationTime()
            );

            jwtSessionRepository.save(jwtSession);
            return jwtSession;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public JwtSession refreshJwtSession(String refreshToken) {
        try {
            if(!validateToken(refreshToken)) {
                throw new JOSEException("Invalid JWT token");
            }

            JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Not found JWT RefreshToken"));

            UUID userId = jwtSession.getUserId();
            UserDto userDto = userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> UserNotFoundException.forId(userId.toString()));

            JwtObject accessJwtObject = createToken(userDto, ACCESS_TOKEN_VALIDITY);
            JwtObject refreshJwtObject = createToken(userDto, REFRESH_TOKEN_VALIDITY);

            jwtSession.updateJwtSession(
                accessJwtObject.token(),
                refreshJwtObject.token(),
                accessJwtObject.expirationTime()
            );

            return jwtSessionRepository.save(jwtSession);

        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void invalidateJwtSession(String refreshToken) {
        jwtSessionRepository.findByRefreshToken(refreshToken)
            .ifPresent(this::invalidate);
    }

    @Transactional
    public void invalidateJwtSession(UUID userId) {
        jwtSessionRepository.findByUserId(userId)
            .ifPresent(this::invalidate);
    }

    @Transactional(readOnly = true)
    public JwtSession getJwtSession(String refreshToken) {
        return jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Not found JWT RefreshToken"));
    }

    @Transactional(readOnly = true)
    public List<JwtSession> findAllActiveJwtSessions() {
        return jwtSessionRepository.findAllByExpiresAtAfter(Instant.now());
    }

    private JwtObject createToken(
        UserDto userDto, Duration expirationMinutes
    ) {
        Instant now = Instant.now();
        Instant expirationTime = Instant.now().plus(expirationMinutes);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userDto.username())
            .claim("userDto", userDto)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(expirationTime))
            .build();

        SignedJWT jwt = new SignedJWT(
            new JWSHeader(JWSAlgorithm.HS256),
            claimsSet
        );

        try {
            jwt.sign(new MACSigner(secret));
            return JwtObject.toJwtObject(jwt);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token) {
        Date now = new Date();

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verified = signedJWT.verify(new MACVerifier(secret));
            if (!verified) {
                throw new SecurityException("Invalid token signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expirationTime = claims.getExpirationTime();
            if (now.after(expirationTime)) {
                throw new SecurityException("Token expired");
            }

            if(blackList.check(token)) {
                throw new SecurityException("Blacklisted");
            }

            return true;
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void invalidate(JwtSession jwtSession) {
        jwtSessionRepository.delete(jwtSession);
        blackList.put(jwtSession.getAccessToken(), jwtSession.getExpiresAt());
    }

}
