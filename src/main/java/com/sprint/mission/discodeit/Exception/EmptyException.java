package com.sprint.mission.discodeit.Exception;

public class EmptyException extends RuntimeException {
    public EmptyException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
