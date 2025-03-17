package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class UserStatus extends BaseEntity {

    private final UUID userid;

    public UserStatus(UUID userid) {
        this.userid = userid;
    }

    public boolean isUserOnline() {
        return Duration.between(getUpdatedAt(), Instant.now()).getSeconds() < 300;
    }


}
