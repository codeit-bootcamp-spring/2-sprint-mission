package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // Auth
  LOGIN_FAILED_USERNAME("로그인 실패 - username 불일치", HttpStatus.UNAUTHORIZED),
  LOGIN_FAILED_PASSWORD("로그인 실패 - password 불일치", HttpStatus.UNAUTHORIZED),

  // BinaryContent
  BINARYCONTENT_PROCESS_FAILD("파일 처리를 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  BINARYCONTENT_NOT_FOUND("파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_BINARYCONTENT_ID("해당 ID의 파일이 이미 존재합니다.", HttpStatus.CONFLICT),

  // Channel
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  PRIVATE_CHANNEL_CANT_UPDATE("private 채널은 수정할 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),

  // Message
  MEESAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // ReadStatus
  DUPLICATE_READSTATUS("이미 존재하는 readStatus 입니다.", HttpStatus.CONFLICT),
  READSTATUS_NOT_FOUND("readStatus를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // Storage
  CANT_CRATE_DIRECTORY("디렉토리를 생성할 수 없습니다.", HttpStatus.CONFLICT),
  CANT_FILE_WRITE("파일 쓰기를 할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  CANT_FILE_READ("파일 읽기를 할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INPUTSTREAM_FAILED("inputStream 열기 실패.", HttpStatus.INTERNAL_SERVER_ERROR),

  // User
  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_EMAIL("이미 존재하는 email입니다.", HttpStatus.CONFLICT),
  DUPLICATE_USERNAME("이미 존재하는 username입니다.", HttpStatus.CONFLICT),

  // UserStatus
  DUPLICATE_USERSTATUS("이미 존재하는 userStatus 입니다.", HttpStatus.CONFLICT),
  USERSTATUS_NOTFOUND("userStatus를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
