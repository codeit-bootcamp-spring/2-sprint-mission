package com.sprint.mission.discodeit.dto.service.auth;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import java.time.Instant;
import java.util.UUID;

public record LoginDTO(
    UUID id,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    Boolean isLogin
) {

}
