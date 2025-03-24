package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        byte[] data,
        String contentType) {
}
