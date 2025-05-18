package com.sprint.mission.common.exception;

public enum ErrorCode {

    ERROR_ACCESS_DENIED_TO_BEAN_STORAGE("빈 저장소는 실행도중 꺼낼 수 없습니다"),

    ERROR_CHANNEL_NOT_FOUND_BY_ID("해당 id의 채널이 존재하지 않습니다"),
    ERROR_CHANNEL_PRIVATE_UPDATE_FORBIDDEN("PRIVATE 채널은 업데이트 될 수 없습니다."),

    ERROR_MESSAGE_NOT_FOUND_BY_ID("해당 메세지가 없습니다"),

    ERROR_USER_NOT_FOUND_BY_EMAIL("Email과 일치하는 유저가 없습니다"),
    ERROR_USER_NOT_FOUND_BY_ID("해당 id의 유저가 존재하지 않습니다"),
    ERROR_USER_NOT_FOUND_BY_NAME("해당 이름의 유저가 존재하지 않습니다"),
    ERROR_USER_ALREADY_EXISTS_NAME("이미 존재하는 이름 입니다"),
    ERROR_USER_ALREADY_EXISTS_EMAIL("이미 존재하는 이메일 입니다"),

    ERROR_BINARY_CONTENT_NOT_FOUND("해당 BinaryContent가 존재하지 않습니다.");

    private static final String ERROR = "[ERROR]";
    private final String message;

    ErrorCode(String message) {
        this.message = ERROR + " " + message;
    }

    public String getMessage() {
        return message;
    }

}
