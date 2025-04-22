package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import java.util.UUID;

public record UpdateUserResponseDTO(
    UUID id,
    FindBinaryContentResult profile,
    String username,
    String email,
    Boolean online
) {

}
