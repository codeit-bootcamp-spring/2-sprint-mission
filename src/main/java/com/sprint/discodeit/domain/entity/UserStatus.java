package com.sprint.discodeit.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;


@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant lastLoginTime;
    private String statusType;

    public UserStatus(Instant lastLoginTime, String statusType) {
        this.id = UUID.randomUUID();
        this.lastLoginTime = lastLoginTime;
        this.statusType = statusType;
    }
}
