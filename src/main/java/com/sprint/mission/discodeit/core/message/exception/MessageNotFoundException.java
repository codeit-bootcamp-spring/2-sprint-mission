package com.sprint.mission.discodeit.core.message.exception;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class MessageNotFoundException extends NotFoundException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
