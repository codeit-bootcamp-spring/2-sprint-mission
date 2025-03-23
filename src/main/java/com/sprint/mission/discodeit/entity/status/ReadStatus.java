package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.MainDomain;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends MainDomain {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastCheckedAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastCheckedAt = Instant.now();
    }

    public void updateLastCheckedAt(){
        this.lastCheckedAt = Instant.now();
    }

}
