package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.model.UserStatusType;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus extends BaseEntity {
    private final UUID userId;
    private UserStatusType userStatusType;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public boolean isOnline() {
        Duration duration = Duration.between(Instant.now(), super.getUpdatedAt());
        return (duration.getSeconds() < 300);
    }
}
