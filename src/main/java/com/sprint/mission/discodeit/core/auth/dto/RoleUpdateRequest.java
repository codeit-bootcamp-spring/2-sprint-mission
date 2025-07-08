package com.sprint.mission.discodeit.core.auth.dto;

import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}