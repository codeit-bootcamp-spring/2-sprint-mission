package com.sprint.mission.discodeit.dto.auth;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthResponseDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String username;
    private String email;
    //
    private UUID profileId;
}
