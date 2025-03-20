package com.sprint.mission.discodeit.exception;

// ResultCode를 받아서 예외를 생성 -> 잡을 때 e.get~으로 메시지 확인 가능
public class RestException extends RuntimeException {
    private final ResultCode resultCode;

    public RestException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public int getStatus() {
        return resultCode.getStatus();
    }

    public String getMessage() {
        return resultCode.getMessage();
    }
}
