package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(min = 4, max = 20, message = "사용자 이름은 4자 이상 20자 이하여야 합니다.")
    String newUsername,

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "새 이메일 주소는 100자를 초과할 수 없습니다.")
    String newEmail,

    @Size(min = 4, max = 30, message = "새 비밀번호는 4자 이상 30자 이하여야 합니다.")
    String newPassword
) {

}
