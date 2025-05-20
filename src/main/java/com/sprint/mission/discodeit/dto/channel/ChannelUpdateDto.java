package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record ChannelUpdateDto(
        @Size(max = 100)
        String newName,
        @Size(max = 500)
        String newDescription
) {
}
