package com.sprint.mission.discodeit.event;

import lombok.Getter;
import java.util.UUID;

@Getter
public class MessageDeletedEvent {
    private final UUID messageId;
    
    public MessageDeletedEvent(UUID messageId) {
        this.messageId = messageId;
    }
} 