package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Message 읽음 상태 생성 정보")
public record ReadStatusCreateRequest(
    @NotNull(message = "userId가 null 일 수 없습니다.")
    UUID userId,

    @NotNull(message = "channelId가 null 일 수 없습니다.")
    UUID channelId,

    @NotNull(message = "lastReadAt이 null 일 수 없습니다.")
    Instant lastReadAt
) {

}
