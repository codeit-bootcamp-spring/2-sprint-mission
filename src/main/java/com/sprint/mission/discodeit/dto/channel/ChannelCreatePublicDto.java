package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelCreatePublicDto(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 500)
        String description
) {
}
