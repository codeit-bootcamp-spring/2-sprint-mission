package com.sprint.mission.discodeit.dto.controller.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUserRequestDTO(
        @NotBlank(message = "username은 필수입니다.")
        @Size(min = 6, max = 12, message = "username은 6자 이상 12자 이하여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username은 영어 대소문자와 숫자만 가능합니다.")
        String username,

        @NotBlank(message = "password는 필수입니다.")
        @Size(min = 8, max = 16, message = "password는 8자 이상 16자 이하여야 합니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{}\\[\\]:;\"'<>,.?/]).{8,16}$",
                message = "password는 영어 대소문자, 숫자, 특수문자를 하나씩 포함해야 하며 한글은 사용할 수 없습니다."
        )
        String password,

        @NotBlank(message = "email은 필수입니다.")
        @Email(message = "email 형식이 올바르지 않습니다.")
        String email
) {
}
