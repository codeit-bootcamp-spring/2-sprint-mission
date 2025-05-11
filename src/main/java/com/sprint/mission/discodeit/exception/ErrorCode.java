package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // User
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 사용자입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    PROFILE_UPLOAD_FAILED("프로필 이미지를 저장할 수 없습니다."),

    // UserStatus
    USER_STATUS_NOT_FOUND("사용자 상태 정보를 찾을 수 없습니다."),
    USER_STATUS_ALREADY_EXISTS("이미 상태 정보가 존재하는 사용자입니다."),

    // Channel
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE_DENIED("PRIVATE 채널은 수정할 수 없습니다."),
    PARTICIPANT_USER_NOT_FOUND("채널 참여자를 찾을 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    INVALID_MESSAGE_AUTHOR("사용자 ID는 null일 수 없습니다."),
    INVALID_MESSAGE_CHANNEL("채널 ID는 null일 수 없습니다."),
    ATTACHMENT_SAVE_FAILED("첨부파일 저장 실패"),

    // File
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED("파일 업로드 실패"),

    // ReadStatus
    READ_STATUS_NOT_FOUND("해당 ID의 읽음 상태를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS("이미 읽음 상태가 존재합니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
