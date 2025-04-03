package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateEmailException extends RuntimeException {
    private final HttpStatus status;

    public DuplicateEmailException(String email, HttpStatus status) {
        super("이미 사용 중인 이메일입니다 : " + email);
        this.status = status;
    }

    public DuplicateEmailException(String email, HttpStatus status, Throwable cause) {
        super("이미 사용 중인 이메일입니다 : " + email, cause);
        this.status = status;
    }
}
