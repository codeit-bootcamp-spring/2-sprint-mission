package com.sprint.discodeit.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadMessageTime;
    // True 읽음, False 안 읽음
    private Boolean readCheck;

    public ReadStatus(UUID userId, Instant lastReadMessageTime, Boolean readCheck, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastReadMessageTime = lastReadMessageTime;
        this.readCheck = readCheck;
        this.channelId = channelId;
    }

    public void markAsRead(Instant newReadTime) {
        this.lastReadMessageTime = newReadTime;
        this.readCheck = true;
    }
}
