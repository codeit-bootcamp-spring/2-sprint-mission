package com.sprint.mission.discodeit.exception.legacy;

public class ValidException extends RuntimeException {
    public ValidException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
