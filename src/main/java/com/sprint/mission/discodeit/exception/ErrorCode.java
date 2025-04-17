package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID_PASSWORD(400, "A001", "비밀번호가 일치하지 않습니다."),
    USER_EMAIL_EXISTS(400, "U002", "이미 사용 중인 이메일입니다."),
    USER_NAME_EXISTS(400, "U003", "이미 사용 중인 닉네임입니다."),
    CHANNEL_ALREADY_EXISTS(400, "C002", "이미 존재하는 채널입니다."),
    PRIVATE_CHANNEL_UPDATE_NOT_SUPPORTED(400, "C003", "비공개 채널은 수정할 수 없습니다."),
    READ_STATUS_ALREADY_EXISTS(400, "R002", "이미 읽음 상태가 존재합니다."),
    USER_STATUS_ALREADY_EXISTS(400, "US002", "이미 유저 상태가 존재합니다."),

    // 404 Not Found
    USER_NOT_FOUND(404, "U001", "해당 사용자를 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND(404, "M001", "해당 메시지를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(404, "C001", "해당 채널을 찾을 수 없습니다."),
    BINARY_CONTENT_NOT_FOUND(404, "B001", "파일을 찾을 수 없습니다."),
    READ_STATUS_NOT_FOUND(404, "R001", "읽음 상태 정보를 찾을 수 없습니다."),
    USER_STATUS_NOT_FOUND(404, "US001", "유저 상태 정보를 찾을 수 없습니다."),

    // 500 Internal Error
    INTERNAL_SERVER_ERROR(500, "S001", "내부 서버 오류입니다."),

    BINARY_CONTENT_SAVE_FAILED(500, "B002", "파일 저장에 실패했습니다."),
    BINARY_CONTENT_LOAD_FAILED(500, "B003", "파일 로드에 실패했습니다."),
    BINARY_CONTENT_DOWNLOAD_FAILED(500, "B004", "파일 다운로드에 실패했습니다.");


    private final int httpStatus;
    private final String code;
    private final String message;
}