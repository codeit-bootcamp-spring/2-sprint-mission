package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
    String newUsername,

    @Email(message = "유효한 이메일 양식이 아닙니다.")
    String newEmail,

    String newPassword
) {

}
