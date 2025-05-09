package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "메시지 생성 요청")
public record MessageCreateRequest(
        @NotBlank(message = "메시지 내용은 필수입니다.")
        String content,

        @NotNull(message = "채널 ID는 필수입니다.")
        UUID channelId,

        @NotNull(message = "작성자 ID는 필수입니다.")
        UUID authorId
) {
}
