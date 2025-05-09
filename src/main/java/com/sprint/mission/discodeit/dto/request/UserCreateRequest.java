package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @Schema(description = "사용자명")
        @NotBlank(message = "사용자명은 필수입니다.")
        @Size(min = 2, max = 30, message = "사용자명은 2~30자 사이여야 합니다.")
        String username,

        @Schema(description = "이메일")
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Schema(description = "비밀번호")
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 100, message = "비밀번호는 8~100자 사이여야 합니다.")
        String password
) {
}
