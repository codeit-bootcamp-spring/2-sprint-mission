package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    INFO_DUPLICATE("정보가 이미 존재합니다."),
    INFO_NOT_FOUND("정보가 존재하지 않습니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    FILE_STORAGE_ERROR("파일 오류입니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.");

    private final String message;
}
