package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(UUID userId) {
        super("이미 등록된 userId 입니다 : " + userId.toString());
    }
}
