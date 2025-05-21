package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank(message = "사용자 이름은 비어 있을 수 없습니다.")
    @Size(min = 3, max = 20, message = "사용자 이름은 3자 이상 20자 이하로 입력해야 합니다.")
    String newUsername,

    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    @Email(message = "올바른 이메일 형식으로 입력해야 합니다.")
    String newEmail,

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    String newPassword
) {

}
