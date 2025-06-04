package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "username은 필수 입력값입니다.")
    String username,
    @NotBlank(message = "username은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    String email,
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    String password
) {

}
