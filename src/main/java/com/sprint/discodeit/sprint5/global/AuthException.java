package com.sprint.discodeit.sprint5.global;

public class AuthException extends BaseException{

    protected AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
