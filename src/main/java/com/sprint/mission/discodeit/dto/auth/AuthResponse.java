package com.sprint.mission.discodeit.dto.auth;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class AuthResponse {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String username;
    private String email;
    //
    private UUID profileId;
}
