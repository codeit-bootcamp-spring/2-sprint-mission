package com.sprint.mission.discodeit.dto.service.auth;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record LoginResult(
    UUID id,
    FindBinaryContentResult profile,
    String username,
    Role role,
    String email,
    Boolean online
) {

}
