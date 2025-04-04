package com.sprint.mission.discodeit.service.dto.channel;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelRequest(
    @NotBlank String name,
    String description
) {

}