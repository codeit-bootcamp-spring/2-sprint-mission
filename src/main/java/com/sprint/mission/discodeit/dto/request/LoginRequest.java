package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3자 이상, 50자 이하로 입력해야 합니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자 이상, 100자 이하로 입력해야 합니다.")
    String password
) {

}
