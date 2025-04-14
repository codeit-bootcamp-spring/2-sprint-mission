package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserInfoDto(
    UUID id,
    Instant createAt,
    Instant updateAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

}
