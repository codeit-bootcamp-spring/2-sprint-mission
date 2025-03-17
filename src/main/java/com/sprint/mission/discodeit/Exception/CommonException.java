package com.sprint.mission.discodeit.Exception;

public class CommonException extends RuntimeException {
    public CommonException(CommonCode commonCode) {
        super(commonCode.getMessage());
    }
}
