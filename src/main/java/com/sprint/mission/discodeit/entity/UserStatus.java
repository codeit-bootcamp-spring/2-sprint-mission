package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {

    private final UUID userId;
    private Instant lastActiveAt;
    boolean online;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 작으면 : 5분 이내 -> true
    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 크면 : 5분 초과 -> false
    public boolean currentUserStatus() {
        if (Duration.between(lastActiveAt, Instant.now()).getSeconds() < 300) {
            online = true;
            return true;
        } else {
            online = false;
            return false;
        }
    }

    public void updateLastConnectionTime(Instant LastConnectionTime) {
        this.lastActiveAt = LastConnectionTime;
        currentUserStatus();

    }

    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + userId +
                "\nLastConnectionTime: " + getLastActiveAt() +
                "\nUser Status: " + currentUserStatus();

    }
}
