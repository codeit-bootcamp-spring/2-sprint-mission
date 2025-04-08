package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void readStatusUpdate(Instant newLastReadAt) {
        boolean anyValueUpdated = false;
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }


    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + userId +
                "\nChannel ID: " + channelId+
                "\nLast Read Time: " + getLastReadAt();
    }

}
