package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        @NotBlank
        String content,
        @NotBlank
        UUID channelId,
        @NotBlank
        UUID authorId,
        List<UUID> attachmentIds
) {
}
