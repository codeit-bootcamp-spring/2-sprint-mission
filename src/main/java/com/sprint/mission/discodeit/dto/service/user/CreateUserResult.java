package com.sprint.mission.discodeit.dto.service.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import java.util.UUID;

public record CreateUserResult(
    UUID id,
    FindBinaryContentResult profile,
    String username,
    String email,
    Boolean online
) {

}
