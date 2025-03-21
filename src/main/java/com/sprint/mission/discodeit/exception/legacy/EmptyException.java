package com.sprint.mission.discodeit.exception.legacy;

public class EmptyException extends RuntimeException {
    public EmptyException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
