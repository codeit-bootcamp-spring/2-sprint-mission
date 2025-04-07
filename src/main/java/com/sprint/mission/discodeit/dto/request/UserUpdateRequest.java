package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "수정할 User 정보")
public record UserUpdateRequest(
    @NotBlank(message = "newUsername이 공백일 수 없습니다.")
    String newUsername,

    @NotBlank
    @Email(message = "newEmail이 유효한 이메일 형식이 아닙니다.")
    String newEmail,

    @NotBlank(message = "newPassword가 공백일 수 없습니다.")
    String newPassword
) {

}
