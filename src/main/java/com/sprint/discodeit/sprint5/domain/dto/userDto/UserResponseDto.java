package com.sprint.discodeit.sprint5.domain.dto.userDto;

import java.util.UUID;

public record UserResponseDto(UUID profileId, String username, String email, String statusTye) {
}
