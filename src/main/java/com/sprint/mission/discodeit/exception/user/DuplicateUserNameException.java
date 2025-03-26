package com.sprint.mission.discodeit.exception.user;

public class DuplicateUserNameException extends RuntimeException {
    public DuplicateUserNameException(String userName) {
        super("이미 사용 중인 사용자 이름입니다 : " + userName);
    }
}
