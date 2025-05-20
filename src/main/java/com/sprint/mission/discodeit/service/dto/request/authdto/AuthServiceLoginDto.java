package com.sprint.mission.discodeit.service.dto.request.authdto;

import jakarta.validation.constraints.NotBlank;

public record AuthServiceLoginDto(
        @NotBlank String username,
        @NotBlank String password
) {
}