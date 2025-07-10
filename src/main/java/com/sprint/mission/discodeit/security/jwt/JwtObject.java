package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.time.Instant;

public record JwtObject(
    Instant iat,
    Instant exp,
    UserDto userDto,
    String token
) {

}
