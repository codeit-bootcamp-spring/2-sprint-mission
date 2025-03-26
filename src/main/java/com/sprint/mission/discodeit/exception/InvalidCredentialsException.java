package com.sprint.mission.discodeit.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("username 또는 password가 올바르지 않습니다.");
    }
}
