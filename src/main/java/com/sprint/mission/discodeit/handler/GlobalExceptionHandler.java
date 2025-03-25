package com.sprint.mission.discodeit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    //@ExceptionHandler(IllegalArgumentException.class)
    //public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    //    Map<String, Object> body = new LinkedHashMap<>();
    //    body.put("timestamp", LocalDateTime.now());
    //    body.put("status", HttpStatus.BAD_REQUEST.value());
    //    body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
    //    body.put("message", ex.getMessage());
    //
    //    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    //}
    //
    //@ExceptionHandler(NullPointerException.class)
    //public ResponseEntity<Object> handleNullPointException(NullPointerException ex) {
    //    Map<String, Object> body = new LinkedHashMap<>();
    //    body.put("timestamp", LocalDateTime.now());
    //    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    //    body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    //    body.put("message", ex.getMessage());
    //
    //    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    //}
    //
    //@ExceptionHandler(Exception.class)
    //public ResponseEntity<Object> handleGeneralException(Exception ex) {
    //    Map<String, Object> body = new LinkedHashMap<>();
    //    body.put("timestamp", LocalDateTime.now());
    //    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    //    body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    //    body.put("message", ex.getMessage());
    //
    //    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    //}
}
