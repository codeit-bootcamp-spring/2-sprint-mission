package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class RemoveChannelFromUsersEvent {
    private final UUID channelId;
    
    public RemoveChannelFromUsersEvent(UUID channelId) {
        this.channelId = channelId;
    }
} 