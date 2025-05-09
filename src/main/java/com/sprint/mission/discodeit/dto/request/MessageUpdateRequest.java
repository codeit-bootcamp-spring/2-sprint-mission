package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "메시지 업데이트 요청")
public record MessageUpdateRequest(
        @NotBlank(message = "새로운 메시지 내용은 필수입니다.")
        String newContent
) {
}
