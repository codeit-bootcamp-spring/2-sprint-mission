package com.sprint.mission.discodeit.service.BinaryDto;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID contentId,
        byte[] data
        ) {
}
