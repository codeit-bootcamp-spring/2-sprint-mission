package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userid;
    private Instant lastLogin;

    public void update(Instant newLastReadAt){
        lastLogin = newLastReadAt;
    }

    public boolean isLogin(){
        // getEpochSecond 흐른 초를 반환 (5분 x 60초 = 300)
        Instant now = Instant.now();
        long diffSeconds = now.getEpochSecond() - lastLogin.getEpochSecond();
        return diffSeconds < 300;
    }
}
