package com.sprint.mission.discodeit.security.jwt;

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
    public static JwtObject toJwtObject(SignedJWT jwt, UserDto userDto) throws ParseException {
        var claims = jwt.getJWTClaimsSet();

        return new JwtObject(
            claims.getIssueTime().toInstant(),
            claims.getExpirationTime().toInstant(),
            userDto,
            jwt.serialize()
        );
    }
}
