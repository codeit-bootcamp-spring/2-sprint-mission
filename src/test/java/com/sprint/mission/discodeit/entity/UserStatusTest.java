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
        userStatus = new UserStatus(UUID.randomUUID());
        userStatus.updateLastLoginAt();
    }

    @DisplayName("로그인후 제한시간이내에 로그아웃 하지않으면 로그인 상태로 변경합니다.")
    @Test
    void isLogin() {
        assertThat(userStatus.isLogin(Instant.now())).isTrue();
    }

    @DisplayName("로그아웃 이후로 5분내에 로그인한 기록이 없으면 로그아웃 처리합니다.")
    @Test
    void isLogout() {
        Instant instant = ZonedDateTime.now()
                .plusNanos(5L * 60 * 1_000_000_000 + 1_000_000)
                .toInstant();

        assertThat(userStatus.isLogin(instant)).isFalse();
    }
}