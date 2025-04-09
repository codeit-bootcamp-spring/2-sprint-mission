package com.sprint.discodeit.sprint5.global;

public class AuthException extends BaseException{

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
