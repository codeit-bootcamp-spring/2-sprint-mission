package com.sprint.mission.discodeit.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 사용자입니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    BINARY_CONTENT_NOT_FOUND("파일을 찾을 수 없습니다."),
    USER_STATUS_NOT_FOUND("사용자 상태 정보를 찾을 수 없습니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),
    READ_STATUS_NOT_FOUND("읽음 상태 정보를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS("중복된 읽음 상태입니다."),
    BINARY_METADATA_UPLOAD_ERROR("파일 메타데이터 저장 중 오류가 발생했습니다."),
    BINARY_DATA_UPLOAD_STORAGE_ERROR("파일 저장 중 오류가 발생했습니다."),
    BINARY_STORAGE_MAKE_DIR_ERROR("파일 스토리지 디렉토리 생성 중 오류가 발생했습니다."),
    BINARY_STORAGE_PUT_ERROR("파일 스토리지 저장 중 오류가 발생했습니다."),
    BINARY_STORAGE_GET_ERROR("파일 스토리지 로드 중 오류가 발생했습니다."),
    BINARY_STORAGE_DOWNLOAD_ERROR("파일 다운로드 중 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}