package com.sprint.mission.discodeit.exception.common;

import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.exception.ReadStatusException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.exception.UserStatusException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ErrorResponse> handleInvalidUserPassword(AuthException e) {
    if (e.getErrorCode() == ErrorCode.INVALID_USER_PASSWORD) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.UNAUTHORIZED.value()
      );
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    throw e;
  }

  @ExceptionHandler(ChannelException.class)
  public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateNotAllowed(ChannelException e) {
    if (e.getErrorCode() == ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.FORBIDDEN.value()
      );
      return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    throw e;
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserException e) {
    if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFound(UserException e) {
    if (e.getErrorCode() == ErrorCode.USERNAME_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(ChannelException.class)
  public ResponseEntity<ErrorResponse> handleChannelNotFound(ChannelException e) {
    if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(MessageException.class)
  public ResponseEntity<ErrorResponse> handleMessageNotFound(MessageException e) {
    if (e.getErrorCode() == ErrorCode.MESSAGE_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(UserStatusException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusNotFound(UserStatusException e) {
    if (e.getErrorCode() == ErrorCode.USER_STATUS_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(ReadStatusException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusNotFound(ReadStatusException e) {
    if (e.getErrorCode() == ErrorCode.READ_STATUS_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(BinaryContentException.class)
  public ResponseEntity<ErrorResponse> handleBinaryContentNotFound(BinaryContentException e) {
    if (e.getErrorCode() == ErrorCode.BINARY_CONTENT_NOT_FOUND) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    throw e;
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UserException e) {
    if (e.getErrorCode() == ErrorCode.USERNAME_ALREADY_EXISTS) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.CONFLICT.value()
      );
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    throw e;
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(UserException e) {
    if (e.getErrorCode() == ErrorCode.EMAIL_ALREADY_EXISTS) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.CONFLICT.value()
      );
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    throw e;
  }

  @ExceptionHandler(UserStatusException.class)
  public ResponseEntity<ErrorResponse> handleUserStatusAlreadyExists(UserStatusException e) {
    if (e.getErrorCode() == ErrorCode.USER_STATUS_ALREADY_EXISTS) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.CONFLICT.value()
      );
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    throw e;
  }

  @ExceptionHandler(ReadStatusException.class)
  public ResponseEntity<ErrorResponse> handleReadStatusAlreadyExists(ReadStatusException e) {
    if (e.getErrorCode() == ErrorCode.READ_STATUS_ALREADY_EXISTS) {
      Map<String, Object> details = e.getDetails();
      ErrorResponse response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.CONFLICT.value()
      );
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    throw e;
  }
}
