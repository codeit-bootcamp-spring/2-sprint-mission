package com.sprint.mission.discodeit.dto.service.auth;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import java.util.UUID;

public record LoginResult(
    UUID id,
    FindBinaryContentResult profile,
    String username,
    String email,
    Boolean online
) {

}
