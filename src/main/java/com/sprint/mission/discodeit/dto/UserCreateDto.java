package com.sprint.mission.discodeit.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;


public record UserCreateDto(
    @NotNull
    UUID id,

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
