package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;

    private UserStatusType status;

    public UserStatus(UUID userId, UserStatusType status) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.status = status;
    }

    public void update(UserStatusType status) { // 여기서 바꾸면 안되고, 시간을 받으셈
        this.updatedAt = Instant.now();
        this.status = status;
    }

}
