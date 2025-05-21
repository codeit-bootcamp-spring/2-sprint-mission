package com.sprint.mission.discodeit.common.exception;

public enum ErrorCode {

    ERROR_ACCESS_DENIED_TO_BEAN_STORAGE("빈 저장소는 실행도중 꺼낼 수 없습니다"),

    ERROR_CHANNEL_NOT_FOUND("해당 채널이 존재하지 않습니다"),
    ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN("PRIVATE 채널은 업데이트 될 수 없습니다."),

    ERROR_MESSAGE_NOT_FOUND("해당 메세지가 없습니다"),

    ERROR_USER_NOT_FOUND("해당 유저가 존재하지 않습니다"),
    ERROR_USER_ALREADY_EXISTS("이미 존재하는 유저 입니다"),

    ERROR_USER_STATUS_NOT_FOUND("해당유저의 유저상태가 없습니다."),

    ERROR_READ_STATUS_ALREADY_EXISTS("해당 읽기 상태가 이미 존재합니다."),
    ERROR_READ_STATUS_NOT_FOUND("해당 읽기 상태가 없습니다."),

    ERROR_BINARY_CONTENT_NOT_FOUND("해당 BinaryContent가 존재하지 않습니다."),

    ERROR_AUTH_PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.");

    private static final String ERROR = "[ERROR]";
    private final String message;

    ErrorCode(String message) {
        this.message = ERROR + " " + message;
    }

    public String getMessage() {
        return message;
    }

}
