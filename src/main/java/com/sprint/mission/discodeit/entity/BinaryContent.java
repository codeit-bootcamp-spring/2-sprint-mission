package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID profileId;
    private final Instant createdAt;
    private final Path path;

    public BinaryContent(Path path) {
        this.profileId = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.path = path;
    }
}
