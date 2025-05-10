package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

    // null뿐만 아니라 빈 문자열도 체크한다. (NotNull보다 넓은 범위)
    @NotBlank(message = "사용자명 필수 입력")
    @Size(max = 50, message = "사용자 명은 50자 이하")
    String username,

    @NotBlank(message = "이메일 필수 입력")
    @Email(message = "이메일 형식을 지켜주세요")
    @Size(max = 100, message = "이메일은 100자 이하")
    String email,

    @Size(min = 8, max = 60, message = "비밀번호는 8 ~ 60 사이여야한다.")
    @NotBlank(message = "패스워드 필수 입력")
    String password
) {

}
