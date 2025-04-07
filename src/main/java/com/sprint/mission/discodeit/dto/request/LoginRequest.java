package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 정보")
public record LoginRequest(
    @NotBlank(message = "username이 공백일 수 없습니다.")
    String username,

    @NotBlank(message = "password가 공백일 수 없습니다.")
    String password
) {

}
