package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReadStatus {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newlastReadAt) {
        boolean anyValueUpdated = false;
        if(lastReadAt != null&&!newlastReadAt.equals(lastReadAt)) {
            this.lastReadAt = newlastReadAt;
            anyValueUpdated = true;
        }
        if(anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
