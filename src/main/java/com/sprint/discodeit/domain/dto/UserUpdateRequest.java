package com.sprint.discodeit.domain.dto;

import java.util.UUID;

public record UserUpdateRequest(UUID userId, String newUsername, String newEmail, String newPassword, String profileImg) {
}
