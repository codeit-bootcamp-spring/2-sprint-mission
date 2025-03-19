package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class UserStatus extends BaseEntity {

    private static final int ONLINE_THRESHOLD = 300;
    private final UUID userid;
    private UserStatusType status;

    public UserStatus(UUID userid) {
        this.userid = userid;
        this.status = UserStatusType.Online;
    }

    public UserStatusType isUserOnline() {
        if (Duration.between(getUpdatedAt(), Instant.now()).getSeconds() < ONLINE_THRESHOLD) {
            status = UserStatusType.Online;
        } else {
            status = UserStatusType.Offline;
        }
        return status;
    }

    public void updateLastActiveAt() {
        updateTimestamp();
    }

}
