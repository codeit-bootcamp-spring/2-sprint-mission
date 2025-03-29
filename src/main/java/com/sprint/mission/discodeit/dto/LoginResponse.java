package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record LoginResponse(
        @NotBlank
        String email,
        @NotBlank
        String success,
        @NotBlank
        String token
) {
}