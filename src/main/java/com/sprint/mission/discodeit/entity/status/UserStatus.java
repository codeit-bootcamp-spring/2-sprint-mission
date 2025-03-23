package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.MainDomain;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends MainDomain {
    private final UUID userId;
    private Instant lastLogin;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.lastLogin = Instant.now();
    }

    public boolean isActive(){
        return Duration.between(lastLogin, Instant.now()).toMinutes() < 5;
    }

    public void updateLastLogin(){
        lastLogin = Instant.now();
    }

    @Override
    public String toString() {
        return ", isActive: " + isActive();
    }
}
