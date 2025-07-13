package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "유효하지 않은 입력값입니다."),

    // Channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CH001", "채널을 찾을 수 없습니다."),
    CHANNEL_ALREADY_EXISTS(HttpStatus.CONFLICT, "CH002", "이미 존재하는 채널입니다."),
    INVALID_CHANNEL_INFORMATION(HttpStatus.BAD_REQUEST, "CH003", "유효하지 않은 채널 정보입니다."),
    PRIVATE_CHANNEL_UPDATE_DENIED(HttpStatus.FORBIDDEN, "CH004", "개인 채널은 업데이트할 수 없습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다."),
    USER_OPERATION_RESTRICTED(HttpStatus.FORBIDDEN, "U003", "사용자 작업이 제한되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "U004", "인증되지 않은 사용자입니다."),

    // Security
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "S001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "S002", "유효하지 않은 토큰입니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메시지를 찾을 수 없습니다."),
    MESSAGE_FILE_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M002", "메시지 파일 처리 중 오류가 발생했습니다."),

    // File
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "파일을 찾을 수 없습니다."),
    FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F002", "파일 처리 중 오류가 발생했습니다."),

    // ReadStatus
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "RS001", "요청한 읽음 상태 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

