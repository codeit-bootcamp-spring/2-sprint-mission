package com.sprint.mission.discodeit.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
        @NotBlank
        String newName,
        @NotBlank
        String newDescription
) {
}
