package com.sprint.mission.discodeit.core.message.exception;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class ReadStatusNotFoundException extends NotFoundException {
    public ReadStatusNotFoundException(String message) {
        super(message);
    }
}
