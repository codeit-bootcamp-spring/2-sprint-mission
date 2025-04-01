package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        validateReadStatus(userId, channelId);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void update(Instant lastReadAt) {
        if(lastReadAt != null && lastReadAt.isAfter(this.lastReadAt)){
            this.lastReadAt = lastReadAt;
            updateLastModifiedAt();
        }
    }

    private void updateLastModifiedAt() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", lastReadAt=" + lastReadAt +
                '}';
    }

    /*******************************
     * Validation check
     *******************************/
    private void validateReadStatus(UUID userId, UUID channelId){
        // 1. null check
        if (userId == null) {
            throw new IllegalArgumentException("userId가 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("channelId가 없습니다.");
        }
    }

}
