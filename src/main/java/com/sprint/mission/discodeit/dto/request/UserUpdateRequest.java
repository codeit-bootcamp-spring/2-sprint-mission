package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @NotBlank(message = "사용자명 필수 입력")
    @Size(max = 50, message = "사용자 명은 50자 이하")
    String newUsername,

    @NotBlank(message = "이메일 필수 입력")
    @Email(message = "이메일 형식을 지켜주세요")
    @Size(max = 100, message = "이메일은 100자 이하")
    String newEmail,

    @Size(min = 8, max = 60, message = "비밀번호는 8 ~ 60 사이여야한다.")
    @NotBlank(message = "패스워드 필수 입력")
    String newPassword
) {

}
