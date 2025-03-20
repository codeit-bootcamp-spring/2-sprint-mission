package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

@Getter
public class UserStatus implements Serializable{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final Instant craetedAt;
    private Instant updatedAt;
    private Instant lastOnlineAt;
    private String status;

    public UserStatus(UUID userId, Instant lastOnlineAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.craetedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastOnlineAt = lastOnlineAt;
        isCurrentlyOnline();
    }

    public Instant updateLastOnline(Instant lastOnlineAt) {
        this.lastOnlineAt = lastOnlineAt;
        isCurrentlyOnline();
        return lastOnlineAt;
    }

    public void isCurrentlyOnline(){
        Instant now = Instant.now();
        Instant fiveMinuteAgo = now.minusSeconds(5 * 60);

        // 마지막 접속 시간이 5분 전보다 전이면 false, 5분 전보다 후이면 true
        this.status = lastOnlineAt.isAfter(fiveMinuteAgo) ? "online" : "offline";
    }

}
