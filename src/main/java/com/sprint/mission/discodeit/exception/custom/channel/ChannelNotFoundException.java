package com.sprint.mission.discodeit.exception.custom.channel;

import java.util.NoSuchElementException;

public class ChannelNotFoundException extends NoSuchElementException {
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
