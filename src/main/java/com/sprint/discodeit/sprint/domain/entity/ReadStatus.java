package com.sprint.discodeit.sprint.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID usersId;
    private UUID channelId;
    private Instant lastReadMessageTime;
    // True 읽음, False 안 읽음
    private Boolean readCheck;

    @Builder
    public ReadStatus(UUID usersId, Instant lastReadMessageTime, Boolean readCheck, UUID channelId) {
        this.id = UUID.randomUUID();
        this.usersId = usersId;
        this.lastReadMessageTime = lastReadMessageTime;
        this.readCheck = readCheck;
        this.channelId = channelId;
    }

    public void readUpdate(UUID channelId, Boolean readCheck, UUID usersId) {
        if(channelId != null || readCheck != null || usersId != null) {
            this.channelId = channelId;
            this.readCheck = readCheck;
            this.usersId = usersId;
            this.lastReadMessageTime = Instant.now();
        }
    }

}
