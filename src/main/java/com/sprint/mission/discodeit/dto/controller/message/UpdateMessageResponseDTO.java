package com.sprint.mission.discodeit.dto.controller.message;

import com.sprint.mission.discodeit.dto.controller.user.UserResponseDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateMessageResponseDTO(
        UUID id,
        Instant updatedAt,
        List<UUID> attachmentIds,
        String content,
        UUID channelId,
        UserResponseDTO userResponseDTO
) {
}
