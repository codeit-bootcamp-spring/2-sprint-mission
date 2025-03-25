package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserStatusUpdateRequest {
    private Instant newLastActiveAt;

    @Builder
    public UserStatusUpdateRequest(Instant newLastActiveAt) {
        this.newLastActiveAt = newLastActiveAt;
    }
}
