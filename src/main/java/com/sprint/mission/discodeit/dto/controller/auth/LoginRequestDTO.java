package com.sprint.mission.discodeit.dto.controller.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO (
        @NotBlank(message = "username을 입력해주세요.")
        String username,

        @NotBlank(message = "password를 입력해주세요.")
        String password
){
}
