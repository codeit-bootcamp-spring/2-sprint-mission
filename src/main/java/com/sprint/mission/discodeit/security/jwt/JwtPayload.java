package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.UserDto;
import java.time.Instant;

public record JwtPayload(
    Instant issueTime,
    Instant expirationTime,
    UserDto userDto,
    String token
) {

}
