package com.sprint.mission.discodeit.entity.status;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createAt;
    private Instant updatedAt;
    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

    public ReadStatus(UUID channelId, UUID userId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updatedAt = createAt;
        this.lastReadAt = lastReadAt;
        this.channelId = channelId;
        this.userId = userId;
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public void updateChannelId(UUID channelId) {
        this.channelId = channelId;
        this.updatedAt = Instant.now();
    }

    public void updateUserId(UUID userId) {
        this.userId = userId;
        this.updatedAt = Instant.now();
    }
}
