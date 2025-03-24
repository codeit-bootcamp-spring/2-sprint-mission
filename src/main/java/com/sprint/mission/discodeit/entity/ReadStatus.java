package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ReadStatus {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final User user;
    private final Channel channel;
    private final Instant lastReadTime;
}
