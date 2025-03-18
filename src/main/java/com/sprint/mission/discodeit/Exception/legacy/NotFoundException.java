package com.sprint.mission.discodeit.Exception.legacy;

public class NotFoundException extends RuntimeException {
    public NotFoundException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
