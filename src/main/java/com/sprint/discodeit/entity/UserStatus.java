package com.sprint.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserStatus {

    private UUID id;
    private UUID userId;
    private Instant lastLoginTime;

    public String isUserOnline() {
        Instant now = Instant.now();
        Duration duration = Duration.between(lastLoginTime, now);
        if (duration.toMinutes() <= 5) {
            return StatusType.Active.getExplanation();
        } else if (duration.toMinutes() <= 15) {
            return StatusType.Away.getExplanation();
        }
        return StatusType.Inactive.getExplanation();
    }

}
