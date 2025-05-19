package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  //404
  USER_NOT_FOUND(404, "유저가 존재하지 않습니다."),
  CHANNEL_NOT_FOUND(404, "채널이 존재하지 않습니다."),
  MESSAGE_NOT_FOUND(404, "메세지가 존재하지 않습니다."),
  BINARY_CONTENT_NOT_FOUND(404, "콘텐츠가 존재하지 않습니다."),
  READ_STATUS_NOT_FOUND(404, "읽음 상태가 존재하지 않습니다."),
  USER_STATUS_NOT_FOUND(404, "유저 상태가 존재하지 않습니다."),
  USER_NAME_NOT_EXISTS(404, "존재하지 않는 유저 이름입니다."),

  //400
  PRIVATE_CHANNEL_UPDATE(400, "비공개 채널은 수정할 수 없습니다."),
  USER_ALREADY_EXISTS(400, "이미 존재하는 유저입니다."),
  MESSAGE_ALREADY_EXISTS(400, "이미 존재하는 메세지입니다."),
  CHANNEL_ALREADY_EXISTS(400, "이미 존재하는 채널입니다."),
  READ_STATUS_ALREADY_EXISTS(400, "이미 존재하는 읽음 상태입니다."),
  USER_STATUS_ALREADY_EXIXTS(400, "이미 존재하는 유저 상태입니다."),
  BINARY_CONTENT_ALREADY_EXISTS(400, "이미 존재하는 콘텐츠입니다."),
  PASSWORD_MISMATCH(400, "잘못된 비밀번호입니다."),
  BAD_REQUEST(400, "잘못된 요청입니다."),

  //500
  INTERNAL_SERVER_ERROR(500, "서버 에러");

  private final int status;
  private final String message;
}
