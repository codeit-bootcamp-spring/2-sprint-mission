package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatusUpdate {
    private UUID id;
    Instant newActivatedAt;
}
