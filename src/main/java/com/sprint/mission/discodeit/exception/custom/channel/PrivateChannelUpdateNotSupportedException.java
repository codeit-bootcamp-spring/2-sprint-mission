package com.sprint.mission.discodeit.exception.custom.channel;

public class PrivateChannelUpdateNotSupportedException extends IllegalArgumentException {
    public PrivateChannelUpdateNotSupportedException(String message) {
        super(message);
    }
}
