package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class UserJoinedChannelEvent {
    private final UUID userId;
    private final UUID channelId;
    
    public UserJoinedChannelEvent(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }
} 