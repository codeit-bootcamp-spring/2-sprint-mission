package com.sprint.mission.discodeit.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank @Size(min = 1) String password
) {
}
