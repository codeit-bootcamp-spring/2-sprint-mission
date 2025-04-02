package com.sprint.discodeit.sprint5.domain.dto.userDto;

import java.util.UUID;

public record UserUpdateRequestDto(String newUsername, String newEmail, String newPassword, String profileImg) {
}
