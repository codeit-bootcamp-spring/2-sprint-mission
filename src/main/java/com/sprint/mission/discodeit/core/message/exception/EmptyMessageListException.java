package com.sprint.mission.discodeit.core.message.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyMessageListException extends EmptyDataException {
    public EmptyMessageListException(String message) {
        super(message);
    }
}
