package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.utils.TimeUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {

    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadTime) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadTime;
    }

    public void readStatusUpdate(Instant newLastReadTime) {
        boolean anyValueUpdated = false;
        if (newLastReadTime != null && !newLastReadTime.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadTime;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public String getReadAttFormatted() {
        return TimeUtil.convertToFormattedDate(lastReadAt);
    }

    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + userId +
                "\nChannel ID: " + channelId+
                "\nLast Read Time: " + getReadAttFormatted();
    }

}
