package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
  CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "프라이빗 채널은 업데이트할 수 없습니다."),
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지를 찾을 수 없습니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 상태를 찾을 수 없습니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "읽음 상태를 찾을 수 없습니다."),
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "바이너리 파일을 찾을 수 없습니다."),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

}