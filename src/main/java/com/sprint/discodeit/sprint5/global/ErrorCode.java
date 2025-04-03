package com.sprint.discodeit.sprint5.global;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 요청 오류
    INVALID_REQUEST("REQ001", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("REQ002", "필수 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_USERNAME("REQ003", "이미 존재하는 사용자 이름입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("REQ004", "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    USER_REQUEST_NULL("REQ005", "사용자 요청 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    PROFILE_IMAGE_NULL("REQ006", "사용자 프로필 이미지 정보가 없습니다.", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND("REQ007", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_STATUS_NOT_FOUND("REQ008", "사용자의 상태 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_DELETED_USER("REQ009", "이미 삭제된 사용자입니다.", HttpStatus.BAD_REQUEST),

    // 인증/인가 오류
    UNAUTHORIZED("AUTH001", "인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("AUTH002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 서버 오류
    INTERNAL_ERROR("SRV001", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SAVE_FAILED("SRV002", "파일 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_API_FAILED("SRV003", "외부 API 요청에 실패했습니다.", HttpStatus.BAD_GATEWAY);

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
