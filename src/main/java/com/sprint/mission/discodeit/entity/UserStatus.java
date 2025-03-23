package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/*
사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다.
사용자의 온라인 상태를 확인하기 위해 활용합니다.
 */

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAt;

    private final UUID userId;
    private Instant lastActiveAt;  // 마지막 접속 시간

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

}
