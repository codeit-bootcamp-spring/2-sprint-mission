package com.sprint.discodeit.sprint.domain.dto.usersDto;

import java.util.UUID;

public record usersResponseDto(UUID profileId, String usersname, String email, String statusTye) {
}
