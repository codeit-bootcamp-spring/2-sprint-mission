package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ChannelCreatePrivateDto(
        @NotNull
        @Size(min = 1)
        List<UUID> participantIds
) {
}
