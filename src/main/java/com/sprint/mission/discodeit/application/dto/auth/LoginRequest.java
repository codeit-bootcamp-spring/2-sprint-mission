package com.sprint.mission.discodeit.application.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String username, @NotNull String password) {
}
