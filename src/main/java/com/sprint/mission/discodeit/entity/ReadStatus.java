package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.utils.TimeUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {

    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadTime;


    public ReadStatus(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = Instant.now();
    }

    public void readStatusUpdate(){
        this.lastReadTime = Instant.now();
    }

    public String getReadAttFormatted() {
        return TimeUtil.convertToFormattedDate(lastReadTime);
    }

    @Override
    public String toString() {
        return "\nID: " + getId() +
                "\nUser ID: " + userId +
                "\nChannel ID: " + channelId+
                "\nLast Read Time: " + getReadAttFormatted();
    }

}
