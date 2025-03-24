package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record AuthResponseDTO(
        UUID id,
        String username) {
}
