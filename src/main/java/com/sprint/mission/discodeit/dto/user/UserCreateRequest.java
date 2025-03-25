package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserCreateRequest(
        @NotBlank(message = "유저네임 입력해주세요")
        String username,

        @NotBlank(message = "이메일 입력해주세요")
        @Email(message = "이메일 포맷이 안 맞아")
        String email,

        @NotBlank(message = "비번 입력 부탁")
        String password,

        UUID profileId
) {
}
