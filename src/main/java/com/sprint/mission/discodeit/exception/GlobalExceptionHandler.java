package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserOperationRestrictedException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserNotFoundException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
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
            ex.getErrorCode().getStatus()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadStatusNotFoundException(
        ReadStatusNotFoundException ex) {
        ErrorResponse body = ErrorResponse.of(
            ex,
            ex.getErrorCode(),
            ex.getErrorCode().getStatus()
        );
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
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
    public ResponseEntity<ErrorResponse> handleConstraintViolationExceptions(
        ConstraintViolationException ex) {
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
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException ex) {
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException ex) {
        
        log.error("HTTP 메서드 지원 안됨: 요청 메서드={}, 지원되는 메서드={}, 요청 URL={}", 
                  ex.getMethod(), ex.getSupportedMethods(), ex.getMessage());
        
        Map<String, Object> details = new HashMap<>();
        details.put("requestedMethod", ex.getMethod());
        details.put("supportedMethods", ex.getSupportedMethods());
        
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

        ErrorResponse body = ErrorResponse.of(
            ex,
            errorCode,
            status,
            details
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex) {
        
        log.error("🚨 MethodArgumentTypeMismatchException 발생!");
        log.error("🚨 파라미터 타입 불일치: 파라미터명={}, 요청값={}, 예상타입={}, 실제타입={}", 
                  ex.getName(), ex.getValue(), ex.getRequiredType(), ex.getValue() != null ? ex.getValue().getClass() : "null");
        log.error("🚨 Stack trace: ", ex);
        
        Map<String, Object> details = new HashMap<>();
        details.put("parameterName", ex.getName());
        details.put("providedValue", ex.getValue() != null ? ex.getValue().toString() : "null");
        details.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        details.put("actualType", ex.getValue() != null ? ex.getValue().getClass().getSimpleName() : "null");
        
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse body = ErrorResponse.of(
            ex,
            errorCode,
            status,
            details
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("🚨 일반 Exception 발생: {}", e.getClass().getSimpleName());
        log.error("🚨 Exception 메시지: {}", e.getMessage());
        log.error("🚨 Exception Stack trace: ", e);

        ErrorResponse body = ErrorResponse.of(
            e,
            ErrorCode.INVALID_REQUEST,
            HttpStatus.INTERNAL_SERVER_ERROR
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body);
    }
}
