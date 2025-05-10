package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateRequest(
    @NotBlank(message = "Message 내용 입력 필수")
    String newContent
) {

}
