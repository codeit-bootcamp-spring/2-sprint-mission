package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class UserStatusNotFoundException extends NotFoundException {
    public UserStatusNotFoundException(String message) {
        super(message);
    }
}
