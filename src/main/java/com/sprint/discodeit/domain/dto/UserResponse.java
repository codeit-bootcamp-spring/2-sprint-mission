package com.sprint.discodeit.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID profileId, String user, String email, String statusTye) {
}
