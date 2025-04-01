package com.sprint.mission.discodeit.core.channel.exception;

import com.sprint.mission.discodeit.core.user.exception.InvalidException;

public class ChannelModificationNotAllowedException extends InvalidException {
    public ChannelModificationNotAllowedException(String message) {
        super(message);
    }
}
