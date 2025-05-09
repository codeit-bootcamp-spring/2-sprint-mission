package com.sprint.mission.discodeit.exception.common;

import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.exception.ReadStatusException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.exception.UserStatusException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      details.put(fieldName, message);
    });

    ErrorResponse response = new ErrorResponse(
        ErrorCode.VALIDATION_ERROR.name(),
        ErrorCode.VALIDATION_ERROR.getMessage(),
        details,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

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
  public ResponseEntity<ErrorResponse> handleChannelException(ChannelException e) {
    Map<String, Object> details = e.getDetails();
    ErrorResponse response;

    if (e.getErrorCode() == ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.FORBIDDEN.value()
      );
      return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
      response = new ErrorResponse(
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
  public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
    Map<String, Object> details = e.getDetails();
    ErrorResponse response;

    if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } else if (e.getErrorCode() == ErrorCode.USERNAME_NOT_FOUND) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } else if (e.getErrorCode() == ErrorCode.USERNAME_ALREADY_EXISTS) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.CONFLICT.value()
      );
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    } else if (e.getErrorCode() == ErrorCode.EMAIL_ALREADY_EXISTS) {
      response = new ErrorResponse(
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
  public ResponseEntity<ErrorResponse> handleUserStatusException(UserStatusException e) {
    Map<String, Object> details = e.getDetails();
    ErrorResponse response;

    if (e.getErrorCode() == ErrorCode.USER_STATUS_NOT_FOUND) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } else if (e.getErrorCode() == ErrorCode.USER_STATUS_ALREADY_EXISTS) {
      response = new ErrorResponse(
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
  public ResponseEntity<ErrorResponse> handleReadStatusException(ReadStatusException e) {
    Map<String, Object> details = e.getDetails();
    ErrorResponse response;

    if (e.getErrorCode() == ErrorCode.READ_STATUS_NOT_FOUND) {
      response = new ErrorResponse(
          e.getErrorCode().name(),
          e.getErrorCode().getMessage(),
          details,
          e.getClass().getSimpleName(),
          HttpStatus.NOT_FOUND.value()
      );
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } else if (e.getErrorCode() == ErrorCode.READ_STATUS_ALREADY_EXISTS) {
      response = new ErrorResponse(
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
}
