package com.sprint.mission.discodeit.Exception;

public enum CommonCode implements Code{
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    SERVER_NOT_FOUND("서버를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다"),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다"),
    BINARY_CONTENT_NOT_FOUND("바이너리 정보를 찾을 수 없습니다"),

    USER_STATUS_NOT_FOUND("유저 상태 정보를 찾을 수 없습니다"),
    READ_STATUS_NOT_FOUND("읽기 정보를 찾을 수 없습니다"),



    DUPLICATE_USER("중복된 유저가 있습니다."),
    DUPLICATE_USER_STATUS("중복된 유저 상태 정보가 있습니다."),
    DUPLICATE_READ_STATUS("중복된 읽기 정보가 있습니다."),


    INVALID_PASSWORD("비밀번호가 틀립니다"),
    File_NOT_FOUND("해당 파일을 찾을 수 없습니다."),

    EMPTY("리스트가 비어있습니다."),
    EMPTY_USER_LIST("유저 리스트가 비어있습니다."),
    EMPTY_SERVER_LIST("서버 리스트가 비어있습니다"),
    EMPTY_CHANNEL_LIST("채널 리스트가 비어있습니다"),
    EMPTY_MESSAGE_LIST("메시지 리스트가 비어있습니다."),
    EMPTY_USER_STATUS_LIST("유저 상태 정보 리스트가 비어있습니다"),
    EMPTY_READ_STATUS_LIST("읽기 정보 리스트가 비어있습니다"),
    EMPTY_BINARY_CONTENT_LIST("바이너리 정보 리스트가 비어있습니다");

    String message;
    CommonCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
