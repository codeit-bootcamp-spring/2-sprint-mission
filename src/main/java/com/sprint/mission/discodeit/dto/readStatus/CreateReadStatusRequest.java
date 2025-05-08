package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequest(

    @NotNull
    UUID userId,

    @NotNull
    UUID channelId,

    Instant lastReadAt
) {

}