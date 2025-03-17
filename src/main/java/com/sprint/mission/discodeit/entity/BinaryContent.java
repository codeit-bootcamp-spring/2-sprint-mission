package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID binaryContentId;
    public final Instant createdAt;

    public BinaryContent() {
        binaryContentId = UUID.randomUUID();
        createdAt = Instant.now();
    }
}
