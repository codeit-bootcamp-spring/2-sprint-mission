package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username은 필수 입력 항목입니다.")
    String username,
    @NotBlank(message = "Password는 필수 입력 항목입니다.")
    String password
) {

}
