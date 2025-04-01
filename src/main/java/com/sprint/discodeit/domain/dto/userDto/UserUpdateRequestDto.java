package com.sprint.discodeit.domain.dto.userDto;

import java.util.UUID;

public record UserUpdateRequestDto(String newUsername, String newEmail, String newPassword, String profileImg) {
}
