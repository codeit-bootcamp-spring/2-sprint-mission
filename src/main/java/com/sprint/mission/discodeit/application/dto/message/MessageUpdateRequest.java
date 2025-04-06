package com.sprint.mission.discodeit.application.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "메세지 수정 요청")
public record MessageUpdateRequest(@NotBlank String newContent) {
}
