package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "메시지 생성 요청")
public record MessageCreateRequest(
        String content,
        UUID channelId,
        UUID authorId
) {
}
