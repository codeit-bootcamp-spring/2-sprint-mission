package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class UserDeletedEvent {
    private final UUID userId;
    
    public UserDeletedEvent(UUID userId) {
        this.userId = userId;
    }
} 