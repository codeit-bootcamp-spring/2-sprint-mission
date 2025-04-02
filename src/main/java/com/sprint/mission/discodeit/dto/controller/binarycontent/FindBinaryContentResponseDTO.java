package com.sprint.mission.discodeit.dto.controller.binarycontent;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindBinaryContentResponseDTO(
        UUID id,
        Instant createdAt,
        String filename,
        long size,
        String contentType,
        byte[] bytes
) {
}
