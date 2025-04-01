package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private String filePath;

    public BinaryContent(String filePath) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filePath = filePath;
    }
}
