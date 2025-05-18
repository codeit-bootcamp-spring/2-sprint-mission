package com.sprint.mission.discodeit.domain.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 2, max = 20, message = "이름은 2~20자 이내여야 합니다.")
        String newUsername,
        @NotBlank(message = "아이디는 필수입니다.")
        @Email
        String newEmail,
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 8, max = 30, message = "비밀번호는 8~30자 이내여야 합니다.")
        String newPassword
) {
}
