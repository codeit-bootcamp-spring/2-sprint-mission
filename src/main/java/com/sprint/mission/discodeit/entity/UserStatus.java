package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.StatusOperation;
import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

@EntityScan
@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 105L;
    private static final long ONLINE_MINUTES = 5;
    private final UUID id; // 클래스 아이디
    private final UUID userId; // 사용자
    private ZonedDateTime lastSeenAt; // 마지막 접속 시간
    private final ZonedDateTime createdAt; // 클래스 생성 시간
    private ZonedDateTime updatedAt; // 업데이트 시간
    private StatusOperation status;
    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastSeenAt = ZonedDateTime.now();
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = null;
    }

    public void updateLastTime() {
        this.updatedAt = ZonedDateTime.now();
    }

    public void setLastSeenAt(ZonedDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
        this.updatedAt = ZonedDateTime.now();

    }
    public void setStatus(StatusOperation statusOperation) {
        this.status = statusOperation;
        updateLastTime();
    }

    public boolean isOnline() {
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.between(lastSeenAt, now);
        return duration.toMinutes() < ONLINE_MINUTES;
    }



}
