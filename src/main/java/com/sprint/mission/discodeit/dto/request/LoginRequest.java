package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "사용자명은 비어 있을 수, 공백일 수 없습니다.")
    String username,

    @NotBlank(message = "사용자명은 비어 있을 수, 공백일 수 없습니다.")
    String password
) {

}
