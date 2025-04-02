package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @NotBlank String name,
    @NotBlank String password
) {

}
