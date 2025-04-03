package com.sprint.discodeit.sprint5.global;

import org.springframework.http.HttpStatus;

public class RequestException extends BaseException {

    public RequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
