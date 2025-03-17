package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.ReadStatusDto.ReadStatusUpdateRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus {
    protected UUID id;
    protected Instant createdAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = Instant.now();

    }
    public void update(ReadStatusUpdateRequest request) {
        this.lastReadAt = Instant.now();
        this.userId = request.userId();
        this.channelId = request.channelId();
    }
}