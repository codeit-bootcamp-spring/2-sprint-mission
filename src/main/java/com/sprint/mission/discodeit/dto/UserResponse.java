package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserResponse(boolean success, UUID id) {
    public static UserResponse of(boolean success, UUID id) {
        return new UserResponse(success, id);
    }
}