package com.sprint.mission.discodeit.exception.binarycontent;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileFindException extends RuntimeException {
    private final HttpStatus status;

    public FileFindException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FileFindException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
