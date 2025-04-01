package com.sprint.mission.discodeit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex) {
        return "요청한 요소를 찾을 수 없습니다: " + ex.getMessage();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNullPointer(NullPointerException ex) {
        return "서버 오류가 발생했습니다: " + ex.getMessage();
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMultipartException(MultipartException ex) {
        return "파일 업로드 중 오류가 발생했습니다: " + ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex) {
        return "예상치 못한 오류가 발생했습니다: " + ex.getMessage();
    }

}
