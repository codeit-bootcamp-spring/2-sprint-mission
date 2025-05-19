package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
    @NotBlank(message = "메시지 내용은 비어있을 수, 공백일 수 없습니다.")
    String newContent
) {

}
