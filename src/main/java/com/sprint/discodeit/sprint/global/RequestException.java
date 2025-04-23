package com.sprint.discodeit.sprint.global;

public class RequestException extends BaseException {

    public RequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
