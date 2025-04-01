package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
