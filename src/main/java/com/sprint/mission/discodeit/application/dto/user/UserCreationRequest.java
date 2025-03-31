package com.sprint.mission.discodeit.application.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserCreationRequest(@NotNull String name, @NotNull String email, @NotNull String password) {
}
