package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        @NotBlank
        String content,
        @NotNull
        UUID channelId,
        @NotNull
        UUID authorId,
        List<UUID> attachmentIds
) {
}
