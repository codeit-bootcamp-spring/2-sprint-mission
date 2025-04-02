package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.controller.BinaryContentController;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.ReadStatusController;
import com.sprint.mission.discodeit.controller.UserController;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class RestExceptionHandler {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(RestException.class)
  public ResponseEntity<ResponseErrorBody> handleRestExceptions(RestException e) {
    logger.error("{} handled by RestExceptionHandler", e.getMessage());

    return ResponseEntity.status(e.getStatus())
        .body(new ResponseErrorBody(e.getStatus(), e.getMessage()));
  }


  // @Valid 어노테이션으로 발생하는 예외
  // 하나의 필드더라도 여러 메시지를 던져야할 수 있으므로, ResultCode로 관리하지 않고 따로 추출해서 보여줌
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorBody> handleValidationException(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.badRequest()
        .body(new ResponseErrorBody(400, message));
  }
}
