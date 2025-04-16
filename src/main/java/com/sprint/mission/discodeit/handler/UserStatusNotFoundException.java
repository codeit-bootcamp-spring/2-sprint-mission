package com.sprint.mission.discodeit.handler;

import java.util.UUID;

public class UserStatusNotFoundException extends RuntimeException {

    public UserStatusNotFoundException(UUID userId) {
        super("UserStatus를 찾을 수 없습니다. userId=" + userId);
    }

}
