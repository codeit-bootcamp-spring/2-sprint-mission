package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class DeleteChannelMessagesEvent {
    private final UUID channelId;
    
    public DeleteChannelMessagesEvent(UUID channelId) {
        this.channelId = channelId;
    }
} 