package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 3, max = 20, message = "새 사용자명은 3자 이상 20자 이하이어야 합니다.")
    String newUsername,

    @Email(message = "새 이메일은 유효한 형식이어야 합니다.")
    String newEmail,

    @Size(min = 6, message = "새 비밀번호는 6자 이상이어야 합니다.")
    String newPassword
) {

}
