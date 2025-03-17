package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.StatusType;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {

    public String getUserStatus(Instant lastLoginTime) {
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
