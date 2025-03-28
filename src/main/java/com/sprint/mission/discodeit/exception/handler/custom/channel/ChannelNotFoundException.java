package com.sprint.mission.discodeit.exception.handler.custom.channel;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
