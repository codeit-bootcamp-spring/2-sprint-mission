package com.sprint.mission.discodeit.entity.status;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long ONLINE_THRESHOLD_MINUTES = 5;
    private static final long SECONDS_PER_MINUTE = 60;
    private static final long ONLINE_THRESHOLD_SECONDS =
        ONLINE_THRESHOLD_MINUTES * SECONDS_PER_MINUTE;


    private final UUID id;
    private final Instant createdAt;
    private UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID userId, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.lastSeenAt = lastSeenAt;
    }

    public boolean isOnline() {
        return Instant.now().minusSeconds(ONLINE_THRESHOLD_SECONDS).isBefore(lastSeenAt);
    }

    public void updateLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt; // 사용자가 접속한 경우 이 값을 주기적으로 업데이트할 예정입니다.
    }
}
