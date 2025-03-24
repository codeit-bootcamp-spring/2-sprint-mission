package com.sprint.mission.discodeit.dto;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword
) {}
