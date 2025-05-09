package com.sprint.mission.discodeit.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Data
public class ResponseErrorBody {

  Instant timestamp;
  String code;
  int status;
  String message;
  Map<String, Object> details;
  String exceptionType;

  public ResponseErrorBody(RestException e) {
    this.timestamp = Instant.now();
    this.code = e.getResultCode().name();
    this.status = e.getResultCode().getStatus();
    this.message = e.getMessage();
    this.details = e.getDetails();
    this.exceptionType = e.getClass().getSimpleName();
  }

  public ResponseErrorBody(Exception e) {
    this.timestamp = Instant.now();
    this.code = "INTERNAL_SERVER_ERROR";
    this.status = 500;
    this.message = "Unexpected error occurred";
    this.details = Map.of();
    this.exceptionType = e.getClass().getSimpleName();
  }

  // @Valid 어노테이션으로 발생하는 예외 (@RequestBody, @ModelAttribute 객체 단위 파라미터 검증 실패)
  // 하나의 필드더라도 여러 메시지를 던져야할 수 있으므로, 따로 추출해서 보여줌
  public ResponseErrorBody(MethodArgumentNotValidException e) {
    this.timestamp = Instant.now();
    this.code = "BAD_REQUEST";
    this.status = 400;
    this.message = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    this.details = Map.of();
    this.exceptionType = e.getClass().getSimpleName();
  }

  public ResponseErrorBody(ConstraintViolationException e) {
    this.timestamp = Instant.now();
    this.code = "BAD_REQUEST";
    this.status = 400;
    this.message = e.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage())
        .collect(Collectors.joining(", "));
    this.details = Map.of();
    this.exceptionType = e.getClass().getSimpleName();
  }

  public ResponseErrorBody(MaxUploadSizeExceededException e) {
    this.timestamp = Instant.now();
    this.code = "PAYLOAD_TOO_LARGE";
    this.status = 413;
    this.message = "File size is too big (max size: " + e.getMaxUploadSize() + ")";
    this.details = Map.of();
    this.exceptionType = e.getClass().getSimpleName();
  }
}
