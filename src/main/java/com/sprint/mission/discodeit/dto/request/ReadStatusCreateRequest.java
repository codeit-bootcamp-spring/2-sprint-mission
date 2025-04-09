package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "읽음 상태 생성 정보")
public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
