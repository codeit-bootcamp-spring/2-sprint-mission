package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
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
