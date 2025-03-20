package com.sprint.mission.discodeit.DTO.User;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {}
