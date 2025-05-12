package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "Channel ID는 필수 입력 값입니다.")
    UUID userId,

    @NotNull(message = "Channel ID는 필수 입력 값입니다.")
    UUID channelId,

    @NotNull(message = "마지막 읽은 시간은 필수 입력 값입니다.")
    Instant lastReadAt
) {

}
