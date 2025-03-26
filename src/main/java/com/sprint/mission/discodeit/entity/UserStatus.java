package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private Instant updatedAt; // 유저의 마지막 접속시간

    @Builder
    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateUserStatus() {
        this.updatedAt = Instant.now();
    }

    public boolean isLoginUser() {
        if(Duration.between(updatedAt, Instant.now()).toMinutes() < 5) {
            return true;
        } else {
            return false;
        }
    }

}
