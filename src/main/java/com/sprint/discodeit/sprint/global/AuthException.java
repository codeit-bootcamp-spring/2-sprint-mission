package com.sprint.discodeit.sprint.global;

public class AuthException extends BaseException{

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
