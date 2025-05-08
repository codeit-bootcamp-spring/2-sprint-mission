package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // 401 Unauthorized
  INVALID_USER_PASSWORD("사용자 비밀번호가 틀렸습니다."),

  // 403 Forbidden
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("비공개 채널은 업데이트할 수 없습니다."),

  // 404 Not Found
  USER_NOT_FOUND("해당 ID의 사용자를 찾을 수 없습니다."),
  USERNAME_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
  CHANNEL_NOT_FOUND("해당 ID의 채널을 찾을 수 없습니다."),
  MESSAGE_NOT_FOUND("해당 ID의 메시지를 찾을 수 없습니다."),
  USER_STATUS_NOT_FOUND("해당 ID의 사용자 상태를 찾을 수 없습니다."),
  READ_STATUS_NOT_FOUND("읽은 상태를 찾을 수 없습니다."),
  BINARY_CONTENT_NOT_FOUND("해당 ID의 바이너리 콘텐츠를 찾을 수 없습니다."),

  // 409 Conflict
  USERNAME_ALREADY_EXISTS("이미 존재하는 사용자 이름입니다."),
  EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다."),
  USER_STATUS_ALREADY_EXISTS("이미 존재하는 사용자 상태입니다."),
  READ_STATUS_ALREADY_EXISTS("이미 존재하는 읽은 상태입니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
