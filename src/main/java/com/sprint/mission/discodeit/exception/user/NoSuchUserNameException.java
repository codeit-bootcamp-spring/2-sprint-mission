package com.sprint.mission.discodeit.exception.user;

public class NoSuchUserNameException extends RuntimeException {
    public NoSuchUserNameException(String name) {
        super("해당 이름을 가진 유저가 존재하지 않습니다 : " + name);
    }
}
