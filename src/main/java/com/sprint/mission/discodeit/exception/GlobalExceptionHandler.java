package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .code("INVALID_REQUEST")
                .message(ex.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .code("MALFORMED_JSON")
                .message("JSON 파싱 오류가 발생했습니다: " + ex.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "유효하지 않은 값",
                        (error1, error2) -> error1
                ));
                
        ErrorResponse response = ErrorResponse.builder()
                .error("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .code("VALIDATION_ERROR")
                .message("입력 값 검증에 실패했습니다")
                .details(errors)
                .build();
                
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("Unauthorized")
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("AUTHENTICATION_FAILED")
                .message(ex.getMessage())
                .build();
                
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(ForbiddenException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("Forbidden")
                .status(HttpStatus.FORBIDDEN.value())
                .code("ACCESS_DENIED")
                .message(ex.getMessage())
                .build();
                
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
            ErrorResponse response = ErrorResponse.builder()
                    .error("Not Found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .code("RESOURCE_NOT_FOUND")
                    .message("요청하신 리소스를 찾을 수 없습니다.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    
    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleDataConflictException(DataConflictException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("Conflict")
                .status(HttpStatus.CONFLICT.value())
                .code("DATA_CONFLICT")
                .message(ex.getMessage())
                .build();
                
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("예상치 못한 서버 오류 발생:", ex);
        ErrorResponse response = ErrorResponse.builder()
                .error("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("SERVER_ERROR")
                .message("서버 내부 오류가 발생했습니다")
                .build();
                
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}



