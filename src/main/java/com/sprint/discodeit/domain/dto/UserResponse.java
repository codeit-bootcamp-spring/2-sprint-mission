package com.sprint.discodeit.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID profileId, String username, String email, String statusTye) {
}
