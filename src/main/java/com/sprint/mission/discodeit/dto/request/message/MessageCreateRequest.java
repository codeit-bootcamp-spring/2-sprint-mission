package com.sprint.mission.discodeit.dto.request.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "메세지 생성 요청")
public record MessageCreateRequest(@NotBlank String content, @NotNull UUID channelId, @NotNull UUID authorId) {
}
