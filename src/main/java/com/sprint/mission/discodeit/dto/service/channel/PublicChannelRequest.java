package com.sprint.mission.discodeit.dto.service.channel;

import jakarta.validation.constraints.NotEmpty;

public record PublicChannelRequest(
    @NotEmpty String name,
    String description
) {

}