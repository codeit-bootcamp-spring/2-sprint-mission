package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "읽음 상태 응답 DTO")
public record ReadStatusDto(
        @Schema(description = "읽음 상태 ID")
        UUID id,

        @Schema(description = "사용자 ID")
        UUID userId,

        @Schema(description = "채널 ID")
        UUID channelId,

        @Schema(description = "마지막 읽은 시간")
        Instant lastReadAt
) {}
