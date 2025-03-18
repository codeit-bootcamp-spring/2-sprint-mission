package com.sprint.mission.discodeit.dto;

public record CreateUserRequest(
        String name,
        String email,
        String profileImageFileName,
        String profileImageFilePath
) {}
