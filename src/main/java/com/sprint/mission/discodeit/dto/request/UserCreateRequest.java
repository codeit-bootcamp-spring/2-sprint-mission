package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserCreateRequest(
    @NotEmpty
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,
    @NotEmpty
    String password,
    @NotEmpty
    String username
) {

}
