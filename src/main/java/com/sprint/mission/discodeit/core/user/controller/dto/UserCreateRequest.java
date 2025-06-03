package com.sprint.mission.discodeit.core.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User 생성 정보")
public record UserCreateRequest(
    @Schema(description = "User username", example = "string")
    @NotBlank @Size(min = 1, max = 20) String username,

    @Schema(description = "User email", example = "string")
    @NotBlank @Email String email,

    @Schema(description = "User password", example = "string")
    @NotBlank @Size(min = 1, max = 20) String password
) {

}
