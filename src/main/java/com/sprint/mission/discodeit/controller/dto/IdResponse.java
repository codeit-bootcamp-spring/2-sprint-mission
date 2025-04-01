package com.sprint.mission.discodeit.controller.dto;

import java.util.UUID;

public record IdResponse(
        boolean success,
        UUID id
) {
    public static IdResponse of(UUID id) {
        return new IdResponse(true, id);
    }
}