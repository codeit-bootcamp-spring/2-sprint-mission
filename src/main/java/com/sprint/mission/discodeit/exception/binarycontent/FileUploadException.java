package com.sprint.mission.discodeit.exception.binarycontent;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileUploadException extends RuntimeException {
    private final HttpStatus status;

    // 상태 코드와 메시지를 받는 생성자
    public FileUploadException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // 상태 코드, 메시지, 원인 예외를 모두 받는 생성자
    public FileUploadException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
