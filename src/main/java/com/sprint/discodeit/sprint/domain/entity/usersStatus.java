package com.sprint.discodeit.sprint.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class usersStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant lastLoginTime;
    private String statusType;

    public usersStatus(Instant lastLoginTime, String statusType) {
        this.id = UUID.randomUUID();
        this.lastLoginTime = lastLoginTime;
        this.statusType = statusType;
    }

    public void updateStatus(Instant lastLoginTime, String statusType) {
        if(statusType != null) {
            this.statusType = statusType;
            this.lastLoginTime = lastLoginTime;
        }
    }
}
