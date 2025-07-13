package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
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
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
  @Value("${security.jwt.secret}")
  private String secret;
  @Value("${security.jwt.access-token-validity-seconds}")
  private long accessTokenValiditySeconds;
  @Value("${security.jwt.refresh-token-validity-seconds}")
  private long refreshTokenValiditySeconds;

  private final JwtSessionRepository jwtSessionRepository;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  public JwtSession registerJwtSession(UserDto userDto) {

    String accessToken = generateJwtToken(userDto, accessTokenValiditySeconds);
    String refreshToken = generateJwtToken(userDto, refreshTokenValiditySeconds);

    Instant issueTime = Instant.now();
    Instant expirationTime = issueTime.plus(Duration.ofSeconds(accessTokenValiditySeconds));

    JwtSession jwtSession = new JwtSession(userDto.id(), accessToken,
        refreshToken, expirationTime);
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  public boolean validate(String token) {
    try {
      // 1. 토큰 파싱 및 서명 검증
      JWSObject jwsObject = JWSObject.parse(token);
      JWSVerifier verifier = new MACVerifier(secret);

      if (!jwsObject.verify(verifier)) {
        return false;
      }

      // 2. Payload에서 직접 Claim 값 추출
      Payload payload = jwsObject.getPayload();
      Map<String, Object> claims = payload.toJSONObject();

      Object expObj = claims.get("exp");
      if (expObj == null) {
        return false;
      }

      Instant expirationTime = objectMapper.convertValue(expObj, Instant.class);
      if (expirationTime.isBefore(Instant.now())) {
        return false;
      }

      return true;

    } catch (JOSEException | ParseException e) {
      log.error("JWT 유효성 검증 실패: {}", e.getMessage());
      return false;
    }
  }

  @Transactional
  public JwtSession refreshJwtSession(String refreshToken) {
    if (!validate(refreshToken)) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
    }
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND));

    UUID userId = session.getUserId();

    jwtSessionRepository.delete(session);

    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    return registerJwtSession(userDto);
  }

  private String generateJwtToken(UserDto userDto, long tokenValiditySeconds) {
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
      log.error(e.getMessage());
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN_SECRET, e);
    }

    return signedJWT.serialize();
  }

}
