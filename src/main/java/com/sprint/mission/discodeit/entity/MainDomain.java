package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class MainDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public MainDomain() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        update();
    }

    protected void update() {
        this.updatedAt = Instant.now();
    }
}
