package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class UserStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int ONLINE_THRESHOLD = 300;
    private final UUID userid;
    private UserStatusType status;

    public UserStatus(UUID userid) {
        super();
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
