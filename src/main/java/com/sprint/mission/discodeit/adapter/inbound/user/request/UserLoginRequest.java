package com.sprint.mission.discodeit.adapter.inbound.user.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @NotBlank String name,
    @NotBlank String password
) {

}
