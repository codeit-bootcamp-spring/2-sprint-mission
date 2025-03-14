package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public class Server implements Serializable {
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private final UUID serverId;

    @ToString.Include
    private final UUID userOwnerId;

    @ToString.Include
    private String name;

    public final Instant createdAt;
    public Instant updatedAt;



    public Server(UUID userOwnerId, String name) {
        this(UUID.randomUUID(), userOwnerId, Instant.now(), name);
    }

    public Server(UUID serverId, UUID userOwnerId, Instant createdAt, String name) {
        this.serverId = serverId;
        this.userOwnerId = userOwnerId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}

