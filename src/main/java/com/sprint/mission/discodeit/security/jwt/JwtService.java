package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${discodeit.security.jwt.secret}")
    private String secret;
    @Value("${discodeit.security.jwt.access-token-validity-seconds}")
    private long accessTokenValiditySeconds;
    @Value("${discodeit.security.jwt.refresh-token-validity-seconds}")
    private long refreshTokenValiditySeconds;

    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public JwtSession createJwtSession(UserDto userDto) {
        JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

        JwtSession jwtSession = new JwtSession(userDto.id(), accessJwtObject.token(), refreshJwtObject.token(), accessJwtObject.expirationTime());

        return jwtSessionRepository.save(jwtSession);
    }

    private JwtObject generateJwtObject(UserDto userDto, long tokenValiditySeconds) {
        Instant issueTime = Instant.now();
        Instant expirationTime = issueTime.plus(Duration.ofSeconds(tokenValiditySeconds));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDto.username())
                .claim("userDto", userDto)
                .issueTime(new Date(issueTime.toEpochMilli()))
                .expirationTime(new Date(expirationTime.toEpochMilli()))
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(new MACSigner(secret));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        String token = signedJWT.serialize();

        return new JwtObject(issueTime, expirationTime, userDto, token);
    }

    public boolean validate(String token) {
        boolean verified;

        try {
            JWSVerifier verifier = new MACVerifier(secret);
            JWSObject jwsObject = JWSObject.parse(token);
            verified = jwsObject.verify(verifier);

            if (verified) {
                JwtObject jwtObject = parse(token);
                verified = !jwtObject.isExpired();
            }

        } catch (JOSEException | ParseException e) {
            verified = false;
        }

        return verified;
    }

    public JwtObject parse(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            Map<String, Object> jsonObject = payload.toJSONObject();
            return new JwtObject(
                    objectMapper.convertValue(jsonObject.get("iat"), Instant.class),
                    objectMapper.convertValue(jsonObject.get("exp"), Instant.class),
                    objectMapper.convertValue(jsonObject.get("userDto"), UserDto.class),
                    token
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public JwtSession refreshJwtSession(String refreshToken) {
        if (!validate(refreshToken)) {
            throw new RuntimeException();
        }
        JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException());

        UUID userId = parse(refreshToken).userDto().id();
        UserDto userDto = userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> UserNotFoundException.withId(userId));
        JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
        JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

        session.update(
                accessJwtObject.token(),
                refreshJwtObject.token(),
                accessJwtObject.expirationTime()
        );

        return session;
    }


}
