package com.sprint.mission.discodeit.service.userDto;

import java.util.UUID;

public record AuthResponse (
        UUID userId,
        String username,
        String email){


    public AuthResponse(UUID userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    // Getter 추가
}
