package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메시지 업데이트 요청")
public record MessageUpdateRequest(
        String newContent
) {
}
