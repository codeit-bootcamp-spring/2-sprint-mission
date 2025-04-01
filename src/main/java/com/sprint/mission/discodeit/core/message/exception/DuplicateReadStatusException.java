package com.sprint.mission.discodeit.core.message.exception;

import com.sprint.mission.discodeit.core.user.exception.InvalidException;

public class DuplicateReadStatusException extends InvalidException {
    public DuplicateReadStatusException(String message) {
        super(message);
    }
}
