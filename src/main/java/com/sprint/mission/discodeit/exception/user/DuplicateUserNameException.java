package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateUserNameException extends RuntimeException {
    private final HttpStatus status;

    public DuplicateUserNameException(String userName, HttpStatus status) {
        super("이미 사용 중인 사용자 이름입니다 : " + userName);
        this.status = status;
    }

    public DuplicateUserNameException(String userName, HttpStatus status, Throwable cause) {
        super("이미 사용 중인 사용자 이름입니다 : " + userName, cause);
        this.status = status;
    }
}
