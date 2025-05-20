package com.sprint.mission.discodeit.exception.Basic;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404,"존재하지 않는 사용자입니다."),
    DUPLICATE_USER(409, "이미 존재하는 사용자 이름 또는 이메일입니다."),
    CHANNEL_NOT_FOUND(404, "존재하지 않는 채널입니다."),
    PRIVATE_CHANNEL_UPDATE(400, "비공개 채널은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND(404, "존재하지 않는 메세지입니다."),
    BINARY_CONTENT_NOT_FOUND(404, "존재하지 않는 파일입니다."),
    FILE_STORAGE_ERROR(500, "파일 저장 중 오류가 발생했습니다."),
    INTERNAL_ERROR(500, "서버 내부 오류가 발생했습니다.");


    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }


}
