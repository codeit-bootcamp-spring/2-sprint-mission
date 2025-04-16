package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "메시지 응답 DTO")
public record MessageDto(
        @Schema(description = "메시지 ID")
        UUID id,

        @Schema(description = "생성 시간")
        Instant createdAt,

        @Schema(description = "수정 시간")
        Instant updatedAt,

        @Schema(description = "메시지 내용")
        String content,

        @Schema(description = "채널 ID")
        UUID channelId,

        @Schema(description = "작성자")
        UserDto author,

        @Schema(description = "첨부 파일 목록")
        List<BinaryContentDto> attachments
) {}

