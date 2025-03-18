package com.sprint.discodeit.service.util;

import com.sprint.discodeit.domain.StatusType;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class UserStatusEvaluator {

    public String determineUserStatus(Instant lastLoginTime) {
        Instant now = Instant.now();
        Duration duration = Duration.between(lastLoginTime, now);

        if (duration.toMinutes() <= 5) {
            return StatusType.Active.getExplanation();
        }
            return StatusType.Away.getExplanation();
    }
}
