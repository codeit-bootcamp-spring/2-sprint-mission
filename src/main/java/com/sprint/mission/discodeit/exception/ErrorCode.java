package com.sprint.mission.discodeit.exception;

public enum ErrorCode {
  // 공통 에러
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
  INVALID_REQUEST("잘못된 요청입니다."),

  // 사용자 관련 에러
  USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
  DUPLICATE_USER("이미 존재하는 사용자입니다."),

  // 채널 관련 에러
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),

  //메세지 관련 에러
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
  MESSAGE_SEND_FAILED("메시지 전송에 실패했습니다."),
  MESSAGE_FOR_CHANNEL("관련 채널을 찾을 수 없습니다."),
  MESSAGE_FOR_AUTH("작성자를 찾을 수 없습니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
