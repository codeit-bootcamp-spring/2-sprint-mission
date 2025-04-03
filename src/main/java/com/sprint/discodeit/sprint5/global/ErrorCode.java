package com.sprint.discodeit.sprint5.global;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 요청 오류
    INVALID_REQUEST("ERR001", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("ERR002", "필수 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),

    // 인증/인가 오류
    UNAUTHORIZED("AUTH001", "인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("AUTH002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 서버 오류
    INTERNAL_ERROR("ERR999", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
