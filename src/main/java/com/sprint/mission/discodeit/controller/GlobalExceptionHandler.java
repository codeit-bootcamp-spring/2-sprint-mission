package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
    logger.error("No such element exception occurred: {}", ex.getMessage());
    return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    logger.error("Illegal argument exception occurred: {}", ex.getMessage());
    return new ResponseEntity<>("Invalid input provided", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    logger.error("General exception occurred: {}", ex.getMessage());
    return new ResponseEntity<>("An internal server error occurred",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOException(IOException ex) {
    logger.error("IOException occurred: {}", ex.getMessage());
    return new ResponseEntity<>("Error while processing the request", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    logger.error("Runtime exception occurred: {}", ex.getMessage());
    return new ResponseEntity<>("An internal server error occurred",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
