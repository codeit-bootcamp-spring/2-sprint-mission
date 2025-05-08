package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.utils.Patterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
    @NotBlank(message = "사용자 이름을 입력해 주세요")
    String newUsername,
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = Patterns.PASSWORD, message = "비밀번호는 최소 5자 이상 10자 미만, 영문, 숫자, 특수문자를 포함해야 합니다.")
    String newPassword,
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String newEmail
) {

}
