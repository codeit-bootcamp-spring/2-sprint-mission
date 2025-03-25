package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ReadStatusUpdateRequest {
    private Instant newLastReadAt;

    @Builder
    public ReadStatusUpdateRequest(Instant newLastReadAt) {
        this.newLastReadAt = newLastReadAt;
    }
}
