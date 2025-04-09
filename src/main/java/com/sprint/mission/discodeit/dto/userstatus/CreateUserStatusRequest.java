package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserStatusRequest {

    private UUID userId;
    private Instant lastActiveAt;
}
