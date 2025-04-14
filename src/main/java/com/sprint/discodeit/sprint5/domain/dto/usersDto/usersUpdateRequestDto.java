package com.sprint.discodeit.sprint5.domain.dto.usersDto;

import java.util.UUID;

public record usersUpdateRequestDto(String newusersname, String newEmail, String newPassword, String profileImg) {
}
