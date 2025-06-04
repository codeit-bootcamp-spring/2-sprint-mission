package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  //User 에러
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "유저를 찾을 수 없습니다"),
  USER_EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U002", "입력하신 유저의 이메일은 이미 존재하는 이메일 입니다"),
  USER_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U003", "입력하신 유저의 이름은 이미 존재하는 이메일 입니다"),

  //로그인 에러
  AUTH_WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "A001", "해당 비밀번호에 맞는 유저가 존재하지 않습니다"),

  //Channel 에러
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "채널을 찾을 수 없습니다"),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "C002", "PRIVATE 채널은 update 할 수 없습니다"),

  //BinaryContents 에러
  BINARY_CONTENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "Binary Content를 찾을 수 없습니다"),

  //Message 에러
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메시지를 찾을 수 없습니다"),

  //ReadStatus 에러
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "ReadStatus가 존재하지 않습니다"),
  INVALID_READ_STATUS_REQUEST(HttpStatus.BAD_REQUEST, "R001",
      "해당 userId와 channelId에 부합하는 ReadStatus를 찾을 수 없습니다"),

  //UserStatus 에러
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "US001", "UserStatus가 존재하지 않습니다"),

  //예외 에러
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "V001", "요청값이 유효하지 않습니다.");
  
  private final HttpStatus status;
  private String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
