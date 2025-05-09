package com.sprint.mission.discodeit.dto.service.channel;

import jakarta.validation.constraints.NotEmpty;

public record ChannelUpdateRequest(
    @NotEmpty String newName,
    String newDescription
) {

}
