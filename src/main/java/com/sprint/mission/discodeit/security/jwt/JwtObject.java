package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.text.ParseException;
import java.time.Instant;

public record JwtObject (
    Instant issueTime,
    Instant expirationTime,
    UserDto userDto,
    String token
) {
    public static JwtObject toJwtObject(SignedJWT jwt)
        throws ParseException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        var claims = jwt.getJWTClaimsSet();

        String userJson = (String) claims.getClaim("userDto");
        UserDto userDto = objectMapper.readValue(userJson, UserDto.class);

        return new JwtObject(
            claims.getIssueTime().toInstant(),
            claims.getExpirationTime().toInstant(),
            userDto,
            jwt.serialize()
        );
    }
}
