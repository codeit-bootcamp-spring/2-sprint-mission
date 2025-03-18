package com.sprint.mission.discodeit.Exception.legacy;

public class EmptyException extends RuntimeException {
    public EmptyException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
