package com.sprint.discodeit.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadMessageTime;
    // True 읽음, False 안 읽음
    private Boolean readCheck;

    @Builder
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

    public void readUpdate(UUID channelId, Boolean readCheck, UUID userId) {
        if(channelId != null || readCheck != null || userId != null) {
            this.channelId = channelId;
            this.readCheck = readCheck;
            this.userId = userId;
            this.lastReadMessageTime = Instant.now();
        }
    }

}
