package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record RegisterResponse(
    UUID userId,
    boolean success,
    String message
) {

}
