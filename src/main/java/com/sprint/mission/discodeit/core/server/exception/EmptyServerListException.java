package com.sprint.mission.discodeit.core.server.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyServerListException extends EmptyDataException {
    public EmptyServerListException(String message) {
        super(message);
    }
}
