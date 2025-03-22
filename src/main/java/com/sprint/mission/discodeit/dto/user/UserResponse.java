package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String username;
    private String email;
    //
    private UUID profileId;
    private boolean isOnline;
}
