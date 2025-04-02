package com.sprint.mission.discodeit.dto.user.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public record UserCreateDto(
    @NotNull
    String userName,

    @NotNull
    String password,

    @NotNull
    @Email
    String userEmail,

    @Nullable String uploadFileName,
    @Nullable String storeFileName

){

}
