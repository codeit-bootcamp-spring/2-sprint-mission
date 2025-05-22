package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // 사용자 관련 에외 코드
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 상태를 찾을 수 없습니다."),
  DUPLICATE_USER_STATUS(HttpStatus.CONFLICT, "이미 존재하는 사용자 상태입니다."),
  USER_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "사용자명 또는 비밀번호가 올바르지 않습니다."),

  // 채널 관련 예외 코드
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.FORBIDDEN, "비공개 채널은 수정할 수 없습니다."),

  // 메시지 관련 예외 코드
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지 수신 정보를 찾을 수 없습니다."),
  DUPLICATE_READ_STAUS(HttpStatus.CONFLICT, "이미 존재하는 메시지 수신 정보입니다."),

  // 이진 파일 관련 예외 코드
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "이진 파일을 찾을 수 없습니다."),
  UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),

  // 스토리지 관련 예외 코드
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
  FILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 파일입니다.");

  private final HttpStatus httpStatus;
  private final String message;

  ErrorCode (HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
