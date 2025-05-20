package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank(message = "사용자명 필수 입력")
    @Size(max = 50, message = "사용자 명은 50자 이하")
    String username,

    @Size(min = 8, max = 60, message = "비밀번호는 8 ~ 60 사이여야한다.")
    @NotBlank(message = "패스워드 필수 입력")
    String password
) {

}
