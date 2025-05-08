package com.sprint.mission.discodeit.service.dto.request.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String password



) {

}
