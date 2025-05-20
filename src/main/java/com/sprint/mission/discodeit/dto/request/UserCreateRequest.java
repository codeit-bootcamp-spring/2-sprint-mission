package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record UserCreateRequest(
    @NotBlank(message = "이름을 반드시 입력해야 합니다..")
    String username,

    @NotBlank(message = "이메일을 반드시 입력해야 합니다.")
    @Email(message = "유효한 이메일 양식이 아닙니다.")
    String email,

    @NotBlank(message = "비밀번호를 반드시 입력해야 합니다.")
    String password
) {

}
