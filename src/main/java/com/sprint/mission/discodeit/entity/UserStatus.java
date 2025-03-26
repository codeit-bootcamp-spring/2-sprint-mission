package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.utils.TimeUtil;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {

    private final UUID userId;
    private Instant lastConnectionTime;

    public UserStatus(UUID userId, Instant LastConnectionTime) {
        super();
        this.userId = userId;
        this.lastConnectionTime = LastConnectionTime;
    }

    public CurrentStatus currentUserStatus() {
        if (Duration.between(lastConnectionTime, Instant.now()).getSeconds() < 300) {
            return CurrentStatus.ACTIVE;
        } else {
            return CurrentStatus.INACTIVE;
        }
    }

    public void updateLastConnectionTime(Instant LastConnectionTime) {
        this.lastConnectionTime = LastConnectionTime;
    }


    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 작으면 : 5분 이내 -> true
    // 마지막 접속시간과 현재시간을 계산했을 때 300보다 크면 : 5분 초과 -> false
    public String getLastConnectionTimeAtFormatted() {
        return TimeUtil.convertToFormattedDate(lastConnectionTime);
    }


    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + userId +
                "\nLastConnectionTime: " + getLastConnectionTimeAtFormatted() +
                "\nUser Status: " + currentUserStatus();

    }
}
