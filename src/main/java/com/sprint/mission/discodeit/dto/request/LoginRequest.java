package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "name은 필수 입력값입니다.")
    String username,
    @NotBlank(message = "password은 필수 입력값입니다.")
    String password
) {

}
