package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyChannelListException extends EmptyDataException {
    public EmptyChannelListException(String message) {
        super(message);
    }
}
