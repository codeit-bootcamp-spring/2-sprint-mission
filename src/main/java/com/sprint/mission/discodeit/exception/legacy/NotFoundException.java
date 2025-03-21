package com.sprint.mission.discodeit.exception.legacy;

public class NotFoundException extends RuntimeException {
    public NotFoundException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
