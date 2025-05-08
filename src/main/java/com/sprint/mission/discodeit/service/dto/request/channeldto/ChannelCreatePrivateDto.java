package com.sprint.mission.discodeit.service.dto.request.channeldto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record ChannelCreatePrivateDto(
        @NotBlank List<UUID> participantIds
) {
}
