package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // User 관련
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    WRONG_PASSWORD("WRONG_PASSWORD","비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),

    // Channel 관련
    CHANNEL_NOT_FOUND("CHANNEL_NOT_FOUND", "채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED", "비공개 채널은 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // Message 관련
    MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", "메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 파일/BinaryContent 관련
    BINARY_CONTENT_NOT_FOUND("BINARY_CONTENT_NOT_FOUND", "파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_FILE_TYPE("INVALID_FILE_TYPE", "잘못된 파일 형식입니다.", HttpStatus.BAD_REQUEST),

    // 공통
    INVALID_INPUT_VALUE("INVALID_INPUT_VALUE", "잘못된 입력 값입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
