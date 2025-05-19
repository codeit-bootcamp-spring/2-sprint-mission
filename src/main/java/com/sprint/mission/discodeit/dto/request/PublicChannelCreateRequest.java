package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record PublicChannelCreateRequest(
    @NotEmpty String name,
    String description
) {

}
