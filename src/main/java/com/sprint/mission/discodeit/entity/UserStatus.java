package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserStatus {
    private UUID id;
    private UUID userid;
    private Instant lastLogin;

    public boolean isLogin(){
        // getEpochSecond 흐른 초를 반환 (5분 x 60초 = 300)
        if (lastLogin.getEpochSecond() < 300) {
            return true;
        }
        return false;
    }
}
