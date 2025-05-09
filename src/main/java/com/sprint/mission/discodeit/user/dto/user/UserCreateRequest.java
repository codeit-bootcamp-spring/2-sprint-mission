package com.sprint.mission.discodeit.user.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 생성 요청")
public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password) {

}
