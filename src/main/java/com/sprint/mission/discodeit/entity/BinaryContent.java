package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;


@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createAt;
    private final byte[] content;
    private final BinaryContentType type;

    public BinaryContent(BinaryContentType type, byte[] content) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.createAt = Instant.now();
        this.content = content;
    }


}
