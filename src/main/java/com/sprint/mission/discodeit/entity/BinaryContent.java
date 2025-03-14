package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;

    private String resourceLink;

    public BinaryContent(String resourceLink) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.resourceLink = resourceLink;
    }

}
