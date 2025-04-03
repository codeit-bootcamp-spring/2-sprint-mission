package com.sprint.discodeit.sprint5.global;

public class RequestException extends BaseException {

    public RequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
