package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // User
    USER_NOT_FOUND          (HttpStatus.NOT_FOUND,         "해당 사용자를 찾을 수 없습니다."),
    DUPLICATE_USER_EMAIL    (HttpStatus.BAD_REQUEST,       "이미 사용 중인 이메일입니다."),
    DUPLICATE_USER_USERNAME (HttpStatus.BAD_REQUEST,       "이미 사용 중인 사용자 이름입니다."),

    // Channel
    CHANNEL_NOT_FOUND       (HttpStatus.NOT_FOUND,         "해당 채널을 찾을 수 없습니다."),
    DUPLICATE_CHANNEL_NAME  (HttpStatus.BAD_REQUEST,       "이미 존재하는 채널 이름입니다."),
    PRIVATE_CHANNEL_UPDATE  (HttpStatus.BAD_REQUEST,       "비공개 채널은 수정할 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND       (HttpStatus.NOT_FOUND,         "해당 메시지를 찾을 수 없습니다."),

    // File / BinaryContent
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND,         "해당 파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED      (HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    // ReadStatus
    READ_STATUS_NOT_FOUND   (HttpStatus.NOT_FOUND,         "해당 읽음 상태를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS   (HttpStatus.BAD_REQUEST,       "이미 존재하는 읽음 상태입니다."),

    // UserStatus
    USER_STATUS_NOT_FOUND   (HttpStatus.NOT_FOUND,         "해당 사용자 상태를 찾을 수 없습니다."),
    DUPLICATE_USER_STATUS   (HttpStatus.BAD_REQUEST,       "이미 존재하는 사용자 상태입니다."),

    // Auth
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,      "아이디 또는 비밀번호가 올바르지 않습니다."),
    AUTH_UNAUTHORIZED       (HttpStatus.UNAUTHORIZED,      "인증이 필요합니다."),

    // Generic
    BAD_REQUEST             (HttpStatus.BAD_REQUEST,       "잘못된 요청입니다."),
    INTERNAL_ERROR          (HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message    = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
