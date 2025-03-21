package com.sprint.mission.discodeit.exception.legacy;

import lombok.Getter;

@Getter
public enum CommonCode implements Code{
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    SERVER_NOT_FOUND(404,"서버를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(404,"채널을 찾을 수 없습니다"),
    MESSAGE_NOT_FOUND(404,"메시지를 찾을 수 없습니다"),
    BINARY_CONTENT_NOT_FOUND(404,"바이너리 정보를 찾을 수 없습니다"),

    USER_STATUS_NOT_FOUND(404,"유저 상태 정보를 찾을 수 없습니다"),
    READ_STATUS_NOT_FOUND(404,"읽기 정보를 찾을 수 없습니다"),

    DUPLICATE_USER(409,"중복된 유저가 있습니다."),
    DUPLICATE_USER_STATUS(409,"중복된 유저 상태 정보가 있습니다."),
    DUPLICATE_READ_STATUS(409,"중복된 읽기 정보가 있습니다."),


    INVALID_PASSWORD(401,"비밀번호가 틀립니다"),
    File_NOT_FOUND(404,"해당 파일을 찾을 수 없습니다."),

    EMPTY(204,"리스트가 비어있습니다."),
    EMPTY_USER_LIST(204,"유저 리스트가 비어있습니다."),
    EMPTY_SERVER_LIST(204,"서버 리스트가 비어있습니다"),
    EMPTY_CHANNEL_LIST(204,"채널 리스트가 비어있습니다"),
    EMPTY_MESSAGE_LIST(204,"메시지 리스트가 비어있습니다."),
    EMPTY_USER_STATUS_LIST(204,"유저 상태 정보 리스트가 비어있습니다"),
    EMPTY_READ_STATUS_LIST(204,"읽기 정보 리스트가 비어있습니다"),
    EMPTY_BINARY_CONTENT_LIST(204,"바이너리 정보 리스트가 비어있습니다");

    final int status;
    final String message;
    CommonCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
