package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatusCreateRequestDto {
    private UUID userId;
    private Instant activatedAt;
}
