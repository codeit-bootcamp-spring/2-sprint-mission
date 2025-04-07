package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User 생성 정보")
public record UserCreateRequest(
    @NotBlank(message = "username이 공백일 수 없습니다.")
    String username,

    @NotBlank
    @Email(message = "email이 올바른 이메일 형식이 아닙니다.")
    String email,

    @NotBlank(message = "password가 공백일 수 없습니다.")
    String password
) {

}
