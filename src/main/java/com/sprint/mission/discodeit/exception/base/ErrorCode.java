package com.sprint.mission.discodeit.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(404, "해당 유저를 찾을 수 없습니다."),
  USER_STATUS_NOT_FOUND(404, "해당 유저 상태를 찾을 수 없습니다."),
  USERNAME_ALREADY_EXISTS(409, "이미 존재하는 username입니다."),
  EMAIL_ALREADY_EXISTS(409, "이미 존재하는 email입니다."),
  USER_STATUS_ALREADY_EXISTS(409, "해당 유저의 상태가 이미 존재합니다."),
  INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 일치하지 않습니다."),

  CHANNEL_NOT_FOUND(404, "해당 채널을 찾을 수 없습니다."),

  MESSAGE_NOT_FOUND(404, "메시지를 찾을 수 없습니다."),

  BINARY_CONTENT_NOT_FOUND(404, "파일을 찾을 수 없습니다."),
  FILE_LOAD_ERROR(500, "파일 로드 중 오류가 발생했습니다."),

  READSTATUS_NOT_FOUND(404, "해당 ReadStatus를 찾을 수 없습니다."),

  ;

  private final int code;
  private final String message;

}