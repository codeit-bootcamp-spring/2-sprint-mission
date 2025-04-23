package com.sprint.discodeit.sprint.global;

public class ServerException extends BaseException{


    protected ServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
