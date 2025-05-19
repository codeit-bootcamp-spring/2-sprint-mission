package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.auth.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentProcessingException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserDuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.UserDuplicateUserNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ErrorResponseMapper;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  ErrorResponseMapper mapper;

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
    Object userKey = e.getDetails().get(ErrorDetailKey.USER_ID.getKey());
    String keyType = "ID";
    if (userKey == null) {
      userKey = e.getDetails().get(ErrorDetailKey.USER_NAME.getKey());
      keyType = "이름";
    }
    log.error("[USER] 사용자를 찾을 수 없음 - {}: {}", keyType, userKey, e);
    e.printStackTrace(); // 개발 환경에서만 사용
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(UserDuplicateEmailException.class)
  public ResponseEntity<ErrorResponse> handleException(UserDuplicateEmailException e) {
    log.error("[USER] 중복 이메일 - EMAIL: {}", e.getDetails().get(ErrorDetailKey.EMAIL.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(UserDuplicateUserNameException.class)
  public ResponseEntity<ErrorResponse> handleException(UserDuplicateUserNameException e) {
    log.error("[USER] 중복 이름 - USER_NAME: {}", e.getDetails().get(ErrorDetailKey.USER_NAME.getKey()),
        e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ChannelNotFoundException e) {
    log.error("[CHANNEL] 채널을 찾을 수 없음 - ID: {}",
        e.getDetails().get(ErrorDetailKey.CHANNEL_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(ChannelUpdateNotAllowedException.class)
  public ResponseEntity<ErrorResponse> handleException(ChannelUpdateNotAllowedException e) {
    log.error("[CHANNEL] 채널 수정 불가 - ID: {}, 원인: {}",
        e.getDetails().get(ErrorDetailKey.CHANNEL_ID.getKey()), e.getMessage(), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleException(InvalidPasswordException e) {
    log.error("[AUTH] 비밀번호 불일치 - ID: {}", e.getDetails().get(ErrorDetailKey.USER_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(MessageNotFoundException e) {
    log.error("[MESSAGE] 메시지를 찾을 수 없음 - ID: {}",
        e.getDetails().get(ErrorDetailKey.USER_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(BinaryContentProcessingException.class)
  public ResponseEntity<ErrorResponse> handleException(BinaryContentProcessingException e) {
    log.error("[FILE] 프로필 이미지 파일 변환 실패 - 파일 이름: {}",
        e.getDetails().get(ErrorDetailKey.FILENAME.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(BinaryContentNotFoundException e) {
    log.error("[FILE] 바이너리 컨텐츠를 찾을 수 없음 - ID: {}",
        e.getDetails().get(ErrorDetailKey.BINARY_CONTENT_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(UserStatusNotFoundException e) {
    log.error("[USERSTATUS] 사용자 상태를 찾을 수 없음 - ID: {}",
        e.getDetails().get(ErrorDetailKey.USER_STATUS_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(UserStatusAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleException(UserStatusAlreadyExistsException e) {
    log.error("[USERSTATUS] 이미 존재하는 사용자 상태 - ID: {}",
        e.getDetails().get(ErrorDetailKey.USER_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(ReadStatusAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleException(ReadStatusAlreadyExistsException e) {
    log.error("[READSTATUS] 이미 존재하는 읽기 상태 - USER_ID: {}, CANNEL_ID: {}",
        e.getDetails().get(ErrorDetailKey.USER_ID.getKey()),
        e.getDetails().get(ErrorDetailKey.CHANNEL_ID.getKey()), e
    );
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ReadStatusNotFoundException e) {
    log.error("[READSTATUS] 읽기 상태를 찾을 수 없음 - ID: {}",
        e.getDetails().get(ErrorDetailKey.READ_STATUS_ID.getKey()), e);
    e.printStackTrace();
    ErrorResponse response = mapper.toResponse(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    e.printStackTrace();

    Map<String, Object> details = new HashMap<>();
    for (FieldError error : e.getBindingResult().getFieldErrors()) {
      details.put(error.getField(), error.getDefaultMessage());
    }

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "V001",
        "Validation failed.",
        details,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

}
