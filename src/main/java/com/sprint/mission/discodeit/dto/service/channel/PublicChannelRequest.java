package com.sprint.mission.discodeit.dto.service.channel;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelRequest(
    @NotBlank String name,
    String description
) {

}