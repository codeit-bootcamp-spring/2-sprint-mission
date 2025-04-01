package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyUserListException extends EmptyDataException {
    public EmptyUserListException(String message) {
        super(message);
    }
}
