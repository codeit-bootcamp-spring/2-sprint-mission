package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

public record PublicChannelUpdateRequest(
    @NotNull
    String newName,

    @NotNull
    String newDescription
) {

}
