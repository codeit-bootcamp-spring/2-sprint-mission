package com.sprint.mission.discodeit.dto.controller.message;

import jakarta.validation.constraints.NotBlank;

public record UpdateMessageRequestDTO(
        @NotBlank(message = "메시지에 공백은 입력될 수 없습니다.")
        String content
) {

}
