package com.sprint.mission.discodeit.exception.user;

public class NoSuchUserEmailException extends RuntimeException {
    public NoSuchUserEmailException(String email) {
        super("해당 email을 가진 유저가 존재하지 않습니다 : " + email);
    }
}
