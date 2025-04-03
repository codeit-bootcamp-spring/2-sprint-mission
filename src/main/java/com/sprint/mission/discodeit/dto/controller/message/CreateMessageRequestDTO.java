package com.sprint.mission.discodeit.dto.controller.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMessageRequestDTO(
        @NotBlank(message = "메시지에 공백은 입력될 수 없습니다.")
        String content,
        @NotNull(message = "channelId를 입력해주세요.")
        UUID channelId,
        @NotNull(message = "authorId를 입력해주세요.")
        UUID authorId
) {
}
