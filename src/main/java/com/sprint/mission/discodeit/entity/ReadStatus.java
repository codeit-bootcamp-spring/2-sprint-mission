package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID channelId;
    @Builder.Default
    private Instant lastReadAt = Instant.now();

    public void updateLastReadAt() {
        lastReadAt = ZonedDateTime.now(ZoneId.systemDefault()).toInstant();
        super.updateTime();
    }

    public boolean isRead(Instant lastMessageTime) {
        super.updateTime();
        if(lastReadAt == null || lastReadAt.isBefore(lastMessageTime)) {
            return false;
        }
        return true;
    }
}
