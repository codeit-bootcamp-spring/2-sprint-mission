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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }
}
