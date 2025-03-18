package com.sprint.mission.discodeit.Exception;

public class ValidException extends RuntimeException {
    public ValidException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
