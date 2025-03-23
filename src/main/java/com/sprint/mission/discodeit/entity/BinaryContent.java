package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent {
    private UUID id;
    private Instant createdAt;
}
