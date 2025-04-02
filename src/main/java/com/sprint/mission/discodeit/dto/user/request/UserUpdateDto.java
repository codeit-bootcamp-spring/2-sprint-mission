package com.sprint.mission.discodeit.dto.user.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateDto(
        @NotNull
        UUID userId,

        @NotNull
        String userName,

        @NotNull
        String userPassword,

        @Nullable
        @Email
        String userEmail,

        @Nullable String uploadFileName,
        @Nullable String storeFileName

) {
}
