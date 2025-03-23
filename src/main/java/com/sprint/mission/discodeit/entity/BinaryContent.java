package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private final String path;

    public BinaryContent(Path path) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.path = path.toString();
    }
}
