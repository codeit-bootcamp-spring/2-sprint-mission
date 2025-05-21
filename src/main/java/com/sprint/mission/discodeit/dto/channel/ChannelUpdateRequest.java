package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record ChannelUpdateRequest(
        @Size(max = 100)
        String newName,
        @Size(max = 500)
        String newDescription
) {
}
