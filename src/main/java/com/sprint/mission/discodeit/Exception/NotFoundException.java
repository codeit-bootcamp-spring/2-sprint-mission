package com.sprint.mission.discodeit.Exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
