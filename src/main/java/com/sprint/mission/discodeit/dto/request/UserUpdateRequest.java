package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
    @NotBlank(message = "사용자명은 비어있을 수, 공백일 수 없습니다.")
    String newUsername,

    @NotBlank(message = "이메일은 비어있을 수, 공백일 수 없습니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    String newEmail,

    @NotBlank(message = "비밀번호는 비어있을 수, 공백일 수 없습니다.")
    String newPassword
) {

}
