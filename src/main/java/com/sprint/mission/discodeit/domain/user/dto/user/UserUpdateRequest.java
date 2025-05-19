package com.sprint.mission.discodeit.domain.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 20, message = "이름은 2~20자 이내여야 합니다.")
        String newUsername,

        @Email(message = "이메일 형식에 맞지 않습니다.")
        String newEmail,

        @Size(min = 8, max = 30, message = "비밀번호는 8~30자 이내여야 합니다.")
        String newPassword
) {
}
