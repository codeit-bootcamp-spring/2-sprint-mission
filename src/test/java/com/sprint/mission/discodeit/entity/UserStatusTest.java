package com.sprint.mission.discodeit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserStatusTest {

    private static final Instant FIXED_NOW = Instant.parse("2025-04-07T12:00:00Z");
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final long NANOS_PER_MINUTE = 60L * NANOS_PER_SECOND;
    private static final long FIVE_MINUTES_IN_NANO = 5L * NANOS_PER_MINUTE;

    @DisplayName("유저의 마지막 활동 시간이 현재 시간 기준 5분 이내인 경우, 온라인 상태를 반환합니다.")
    @Test
    void isOnline() {
        Instant lastActiveAt = FIXED_NOW;
        UserStatus userStatus = new UserStatus(UUID.randomUUID(), lastActiveAt);

        Instant checkTime = lastActiveAt.plusNanos(FIVE_MINUTES_IN_NANO - NANOS_PER_SECOND);
        assertThat(userStatus.isOnline(checkTime)).isTrue();
    }

    @DisplayName("유저의 마지막 활동 시간에서 5분이 지난 경우, 오프라인 상태를 반환합니다.")
    @Test
    void isNotOnline() {
        Instant lastActiveAt = FIXED_NOW;
        UserStatus userStatus = new UserStatus(UUID.randomUUID(), lastActiveAt);

        Instant checkTime = lastActiveAt.plusNanos(FIVE_MINUTES_IN_NANO + NANOS_PER_SECOND);
        assertThat(userStatus.isOnline(checkTime)).isFalse();
    }
}