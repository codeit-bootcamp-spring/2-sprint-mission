package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class ChannelNotFoundException extends NotFoundException {
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
