package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.data.ErrorResponse;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException e) {
    return ErrorResponse.of(false, "잘못된 입력 입니다: " + e.getMessage());
  }

  @ExceptionHandler(FileProcessingException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse fileProcessingExceptionHandler(FileProcessingException e) {
    return ErrorResponse.of(false, "파일 처리 오류: " + e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse noSuchElementExceptionHandler(NoSuchElementException e) {
    return ErrorResponse.of(false, "찾을 수 없는 요소입니다: " + e.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse nullPointerExceptionHandler(NullPointerException e) {
    return ErrorResponse.of(false, "널 포인터 예외 발생: " + e.getMessage());
  }
}