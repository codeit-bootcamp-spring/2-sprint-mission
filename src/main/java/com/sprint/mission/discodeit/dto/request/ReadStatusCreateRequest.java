package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(

    @NotNull(message = "userId 입력 필수")
    UUID userId,

    @NotNull(message = "channelId 입력 필수")
    UUID channelId,

    @NotNull(message = "마지막으로 읽은 시점 입력 필수")
    Instant lastReadAt
) {

}
