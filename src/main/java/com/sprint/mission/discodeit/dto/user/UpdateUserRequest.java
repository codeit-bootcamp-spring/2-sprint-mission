package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateUserRequest(
    @NotNull(message = "userId는 필수입니다.")
    UUID userId,

    @Size(min = 3, max = 20, message = "사용자 이름은 3자 이상 20자 이하로 입력하세요.")
    String newUsername,

    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    String newPassword,

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String newEmail
) {

}
