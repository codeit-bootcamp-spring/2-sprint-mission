package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record IdResponse(boolean success, UUID id) {
    public static IdResponse of(boolean success, UUID id) {
        return new IdResponse(success, id);
    }
}