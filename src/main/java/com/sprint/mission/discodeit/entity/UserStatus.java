package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private Boolean onlineStatus;

    public UserStatus(UUID id, UUID userId, Boolean onlineStatus) {
        this.id = id;
        this.userId = userId;
        this.onlineStatus = onlineStatus;
    }
}
