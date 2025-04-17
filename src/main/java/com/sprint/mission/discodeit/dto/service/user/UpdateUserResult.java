package com.sprint.mission.discodeit.dto.service.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import java.time.Instant;
import java.util.UUID;

public record UpdateUserResult(
    UUID id,
    Instant updatedAt,
    FindBinaryContentResult profile,
    String username,
    String email,
    Boolean online
) {

}
