package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  DUPLICATE_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "비공개 채널은 수정할 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}