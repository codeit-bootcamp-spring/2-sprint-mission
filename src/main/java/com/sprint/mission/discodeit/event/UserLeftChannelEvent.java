package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class UserLeftChannelEvent {
    private final UUID userId;
    private final UUID channelId;
    
    public UserLeftChannelEvent(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }
} 