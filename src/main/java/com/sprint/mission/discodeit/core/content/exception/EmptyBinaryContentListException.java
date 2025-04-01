package com.sprint.mission.discodeit.core.content.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyBinaryContentListException extends EmptyDataException {
    public EmptyBinaryContentListException(String message) {
        super(message);
    }
}
