package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
  BAD_REQUEST(400, "잘못된 요청입니다."),
  FORBIDDEN(403, "접근이 거부되었습니다."),
  INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),

  USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
  DUPLICATE_USER(409, "이미 존재하는 사용자입니다."),

  CHANNEL_NOT_FOUND(404, "채널을 찾을 수 없습니다."),
  DUPLICATE_CHANNEL(409, "이미 존재하는 채널입니다."),
  PRIVATE_CHANNEL_UPDATE(400, "비공개 채널은 수정할 수 없습니다."),

  MESSAGE_NOT_FOUND(404, "메시지를 찾을 수 없습니다."),
  DUPLICATE_MESSAGE(409, "이미 존재하는 메시지입니다."),

  WRONG_PASSWORD(400, "잘못된 패스워드입니다."),

  BINARYCONTENT_NOT_FOUND(404, "파일을 찾을 수 없습니다."),

  READSTATUS_NOT_FOUND(404, "읽기 상태를 찾을 수 없습니다."),

  USERSTATUS_NOT_FOUND(404, "유저 상태를 찾을 수 없습니다."),
  DUPLICATE_USERSTATUS(409, "이미 존재하는 유저 상태입니다.");

  private final int status;
  private final String message;

  ErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
