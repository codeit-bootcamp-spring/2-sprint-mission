package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;


}
