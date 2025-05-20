package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  NOT_FOUND("요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
  FORBIDDEN("접근이 거부되었습니다.", HttpStatus.FORBIDDEN),
  INTERNAL_SERVER_ERROR("서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_USER("이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
  DUPLICATE_USER_EMAIL("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
  DUPLICATE_USER_USERNAME("이미 존재하는 이름입니다.", HttpStatus.CONFLICT),

  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INVALID_CHANNEL_TYPE("PRIVATE 채널은 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),

  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


  private final String message;
  private final HttpStatus status;
}
