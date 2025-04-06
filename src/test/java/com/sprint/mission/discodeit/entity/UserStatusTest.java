package com.sprint.mission.discodeit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserStatusTest {
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {
        userStatus = new UserStatus(UUID.randomUUID(), Instant.now());
        userStatus.updateLastActiveAt(Instant.now());
    }

    @DisplayName("로그인한 경우, 로그인 상태로 변경합니다.")
    @Test
    void isLogin() {
        assertThat(userStatus.isOnline(Instant.now())).isTrue();
    }

    @DisplayName("로그아웃 5분이 지날 경우, 로그아웃 처리합니다.")
    @Test
    void isLogout() {
        Instant instant = ZonedDateTime.now()
                .plusNanos(5L * 60 * 1_000_000_000 + 1_000_000)
                .toInstant();

        assertThat(userStatus.isOnline(instant)).isFalse();
    }
}