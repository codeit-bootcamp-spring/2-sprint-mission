package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank @Size(min = 1) String password
) {
}
