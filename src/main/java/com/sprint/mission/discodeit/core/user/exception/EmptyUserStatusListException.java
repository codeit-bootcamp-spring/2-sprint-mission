package com.sprint.mission.discodeit.core.user.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyUserStatusListException extends EmptyDataException {
    public EmptyUserStatusListException(String message) {
        super(message);
    }
}
