package com.sprint.mission.discodeit.exception.custom.userStatus;

import java.util.NoSuchElementException;

public class UserStatusNotFoundException extends NoSuchElementException {
    public UserStatusNotFoundException(String message) {
        super(message);
    }
}
