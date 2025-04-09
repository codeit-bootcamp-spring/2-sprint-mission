package com.sprint.discodeit.sprint5.global;

public class ServerException extends BaseException{


    protected ServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
