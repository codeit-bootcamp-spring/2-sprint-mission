package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.InvalidUserStatusUpdateException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserOperationRestrictedException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserNotFoundException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );

        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
        UserAlreadyExistException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );

        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(UserOperationRestrictedException.class)
    public ResponseEntity<ErrorResponse> handleUserOperationRestrictedException(
        UserOperationRestrictedException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(InvalidUserStatusUpdateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserStatusUpdateException(
        InvalidUserStatusUpdateException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(ChannelException.class)
    public ResponseEntity<ErrorResponse> handleChannelException(ChannelException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorResponse> handleMessageException(MessageException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );

        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(FileNotFoundCustomException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundCustomException(
        FileNotFoundCustomException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(FileProcessingCustomException.class)
    public ResponseEntity<ErrorResponse> handleFileProcessingCustomException(
        FileProcessingCustomException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadStatusNotFoundException(ReadStatusNotFoundException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus(),
            ex.getDetails()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> details = new HashMap<>();
        details.put("validationErrors", errors);

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        HttpStatus status = errorCode.getStatus();

        ErrorResponse body = ErrorResponse.of(
            ex,
            errorCode,
            status,
            details
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        Map<String, Object> details = new HashMap<>();
        details.put("validationErrors", errors);

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        HttpStatus status = errorCode.getStatus();

        ErrorResponse body = ErrorResponse.of(
            ex,
            errorCode,
            status,
            details
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("missingParameter", ex.getParameterName());
        details.put("parameterType", ex.getParameterType());

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        HttpStatus status = errorCode.getStatus();

        ErrorResponse body = ErrorResponse.of(
            ex,
            errorCode,
            status,
            details
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }
}
