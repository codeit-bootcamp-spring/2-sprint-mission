package com.sprint.mission.discodeit.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    String newUsername,
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "이메일 형식이 잘못되었습니다")
    String newEmail,
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    String newPassword
) {

}

