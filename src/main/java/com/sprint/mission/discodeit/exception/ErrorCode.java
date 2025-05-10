package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),


    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    INVALID_USER_INFORMATION(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 정보입니다."),


    USER_OPERATION_RESTRICTED(HttpStatus.FORBIDDEN, "해당 사용자에 대한 작업이 제한되었습니다."),
    INVALID_USER_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 상태 변경입니다."),


    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
    CHANNEL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 채널입니다."),
    INVALID_CHANNEL_INFORMATION(HttpStatus.BAD_REQUEST, "유효하지 않은 채널 정보입니다."),
    PRIVATE_CHANNEL_UPDATE_DENIED(HttpStatus.FORBIDDEN, "비공개 채널은 수정할 수 없습니다."),

    CHANNEL_OPERATION_NOT_PERMITTED(HttpStatus.FORBIDDEN, "채널에 대한 작업 권한이 없습니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),
    MESSAGE_TOO_LONG(HttpStatus.BAD_REQUEST, "메시지 내용이 너무 깁니다."),


    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다."),

    // 유효성 검증 실패
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값 유효성 검증에 실패했습니다."),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 읽음 상태 정보를 찾을 수 없습니다.");
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

