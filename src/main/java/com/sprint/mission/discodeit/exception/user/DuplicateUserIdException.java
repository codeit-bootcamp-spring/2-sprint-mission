package com.sprint.mission.discodeit.exception.user;

import java.util.UUID;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(UUID userId) {
        super("이미 사용중인 userId 입니다 : " + userId.toString());
    }
}
